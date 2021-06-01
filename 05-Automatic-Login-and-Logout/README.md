# Spring Security学习笔记（三）—— 自动登录和注销
## 1 自动登录

> 对于一些其他类的设计请参考前几篇文章所提到的，或者进入[https://github.com/LiYangSir/Spring-Security-In-Action](https://github.com/LiYangSir/Spring-Security-In-Action)的第5节直接查看源码。

自动登录主要包含两种：1. 散列加密方式，但是这种方式会存在安全性问题，相应的信息存储在了本地。2. 持久化令牌的方式，在交互上和散列的方式一样将生成的令牌发送到浏览器，并在用户下次访问的时候进行验证。
### 持久化令牌方案
​	在持久化令牌过程中，最核心的是series和token值，他们都采用MD5散列过的随机字符串，不同的是，series仅在用户使用密码重新登陆的时候更新，而token会在每一个新的session中重新生成。
​	这样设计的好处是：1. 每个会话都会触发token的更新，也就是每个token仅支持单实例登录。2. 自动登录不会导致series变更，而每次登录都要验证series和token两个值，当该令牌还没有使用自动登录就被盗取时，系统会在非法用户登录以后刷新token值，此时在合法用户的浏览器中，token已经失效，当合法用户使用自动登录的时候，由于该series对应的token不同，则认为该令牌已经被盗用，可以提醒用户进行相应的操作。

#### 1. 资源准备
自动登录需要将token信息存储到数据库当中，所以需要建立一个专门的数据库，这里采用`JdbcTokenRepositoryImpl`来实现操作数据库。我们点开可以发现有一个创建表的语句，通过这个语句创建数据库。
```sql
create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
```
![[image.png]()](https://markdown-liyang.oss-cn-beijing.aliyuncs.com/blog/elasticsearch/1621865712065-d3d57a3b-11d9-407c-bfdf-94d5b2f284ab.png)
由于操作数据库还需要操作数据库的依赖以及配置

```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
#### 2. 创建UserDetailService
由于进行自动登陆的时候需要通过Token 解析出UserName进一步获取对应的User对象。
```java
package cn.quguai.service;

@Service
public class MyUserDetailService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository; // 使用Spring Data JPA创建UserUserRepository

    @Autowired
    private PasswordEncoder passwordEncoder;  // 需要创建一个Bean: new BCryptPasswordEncoder()放入容器中

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setAuthorityList(AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRoles()));
        return userEntity;
    }
}
```
#### 3. 配置SpringSecurity
```java
@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(true);
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().rememberMe().userDetailsService(userDetailsService).tokenRepository(jdbcTokenRepository).tokenValiditySeconds(60)
                .and().csrf().disable();
    }
}
```
#### 4. 实验
![image.png](https://markdown-liyang.oss-cn-beijing.aliyuncs.com/blog/elasticsearch/1621866267171-166874d6-d233-462f-bff6-fce46d9c0e64.png)
#### 5. 原理总结
Spring Security会对Cookie进行Base64解码，主要是分为两个部分，分别是`series`和`token`，Spring Security通过`series`来从数据库中获取一条数据（因为我们在前面SQL语句中会发现series是作为了主键），来获取实际**真实的token，用户名以及上一次的登录信息。**

1. 通过用户名确认令牌的身份；
1. 通过对比解码后的token和数据库中的token来验证令牌是否有效；
1. 通过上一次的登录信息来获取令牌是否已经过期；
## 2 注销

> 注销相对于登录简单很多，只需要配置注销的相关逻辑即可。

```java
@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(true);
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().rememberMe().userDetailsService(userDetailsService).tokenRepository(jdbcTokenRepository).tokenValiditySeconds(60)
                .and().csrf().disable();
        // 增加处理注销后的逻辑：1. 删除Session  2.删除Cookie
        http.logout().logoutUrl("/myLogout").logoutSuccessUrl("/").logoutSuccessHandler((request, response, authentication) -> {
        }).invalidateHttpSession(true).deleteCookies("cookie").addLogoutHandler((request, response, authentication) -> {});
    }
}
```


