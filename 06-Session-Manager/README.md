# Spring Security学习笔记（五）—— 会话管理

## 1 会话

首先对于Http协议而言是一种无状态的，需要通过Session（会话）来解决，Session技术则主要是服务器发送给浏览器一个ID，浏览器将相关ID保存起来，而对于服务器而言，Session是一种Map结构，通过请求过来的ID找到对应的Value值，这也就形成了不同的请求之间有了。对于ID的保存，在不妨碍体验的情况下，Cookie成了一种不错的载体，将ID存储到其中，保存的形式是key-value的形式，在这里ID对应jsessionid。
尽管Cookie非常有用，但是对于有时用户会在浏览器当中禁用它，这就导致了ID的无法存储，一般的解决方案都是通过URL重写，将ID作为参数写入到URL当中。例如：`http://quguai.cn;jsessionid=xxx`，但是这种重写的方式会带了**固定会话攻击**，攻击方式如下：

1. 黑客首先访问一次系统，那么服务器建立起本次会话，返还给浏览器一个sessionId；
1. 黑客将sessionId写入到URL当中去发给一些取得信任的用户；
1. 用户通过重写的URL进行登录操作的时候，服务器会将用户信息作为value值，sessionId作为key值保存到服务器当中；
1. 黑客就无需进行登录，直接通过sessionId进行一些隐私操作，也就是共享会话；
## 2 防御固定会话攻击
上面所提到的攻击方式的前提就是用户执行了登录操作以后，将用户存储到了URL地址中的sessionId所对应的value当中，解决方案也很简单，就是在登陆请求的时候创建新的sessionId。在SpringSecurity中默认开启。
防御固定会话的方式有4种：

- none：不做任何变动，登录以后沿用旧的session；（不推荐）
- newSession：登录以后创建的一个新的session；
- migrateSession：登录以后创建一个新的Session，并将旧的session中的数据复制出来；（默认）
- changeSessionId：不创建新的会话，而是使用由Servlet容器提供的会话固定攻击；
```java
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin().permitAll()
                .and().sessionManagement()
                .sessionFixation().none()
    }
}
```
在Spring Security 中，即便没有配置，也大可不必担心固定会话攻击，因为Spring Security的Http防火墙会拦截不合法的请求，当我们访问带session的URL的时候会自动重定向。
## 3 会话过期
还可以通过配置当会话过期时跳转到的请求
```javascript
.and().sessionManagement()
      .invalidSessionUrl("/session/invalid") // Session 失效后的定向网址
```
或者可以自定义过期策略
```java
public class MyInvalidSessionStrategy implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("Session 无效");
    }
}
```
```java
.and().sessionManagement()
      .invalidSessionStrategy(new MyInvalidSessionStrategy())
```
我们还可以修改具体的会话过期时间
```yaml
server:
  servlet:
    session:
      timeout: 60s
```
> 如果会话的时间少于1分钟，会自动修正为1分钟，这属于SpringBoot 的配置策略。

## 4 会话并发控制
```java
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin().permitAll()
                .and().sessionManagement()
                .maximumSessions(1)// 最大用户数量
    }
}
```
maximumSessions用户设置单个用户允许同时在线的最大会话数，如果没有额外配置，那么新登录的会话会踢除掉旧的会话，非常类似于**QQ**等一系列通讯工具。
当我们想阻止新会话，而不是剔除新会话，则可以像下面配置。实际的场景就是**游戏登录。**
```java
.and().sessionManagement()
      .maximumSessions(1)  // 最大用户数量
      .maxSessionsPreventsLogin(true) // 组织新会话的连接, QQ登录就是False,游戏登录是True
```
这里其实有**一个坑**，说到这里，其实配置起来很容器，但下面这里会有一个坑，当我们尝试用已经登陆的旧会话进行注销，那么新会话按理来说就可以进行登录，实际上并不行，仍然提示我们出现了超过最大会话数。这里因为Spring Security是通过监听Session的销毁事件进行触发会话信息的相关清理，但是我们并没有注册过相关的监听器，导致Spring Security无法正常清理。
```java
@Bean
public HttpSessionEventPublisher httpSessionEventPublisher(){
    return new HttpSessionEventPublisher();
}
```
![image.png](https://markdown-liyang.oss-cn-beijing.aliyuncs.com/blog/elasticsearch/1622552437872-23b0b915-bd74-42d9-9ff6-b0fc2d9f2604.png)
至此，已经包含了全部的会话管理的基本操作，但是这里**还有一个坑**，在我们实际场景中，最多的还是读取数据库来完成相应的操作，我们沿用前一篇文章里面的自定义数据库完成数据认证的方式，使用MySQL和Spring Data JPA完成相应的操作。下面只贴代码，不进行说明。
```java
package cn.quguai.entity;

@Data
@Entity
@Table(name = "t_user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private Boolean enable;
    private String roles;

    @Transient
    private List<GrantedAuthority> authorityList;

    public void setAuthorityList(List<GrantedAuthority> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
```
```java
package cn.quguai.service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
```java
package cn.quguai.dao;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
```
当我们使用自定义数据完成操作的时候登录不会出现问题，但是当我们进行会话并发控制就会出现问题并不会进行限制，都允许的登录，这是因为SpringSecurity底层保存用户信息采用的HashMap的形式进行存储，而map是以**用户信息**作为key，当我们使用同一个账号进行登陆的时候，他会首先判断在容器当中时候包含这个用户信息，如果有就进行相应的限制，但是我们的用户信息并没有实现hashcode和equals方法，所以会出现同一账户同时登陆。
> hashmap中要求我们对于key值必须重写equals和hashcode两个方法

这也就会产生一种现象，当我们使用官网实例，或者基于内存的用户模型进行测试的时候并不会出现问题。
> 具体可以参考`org.springframework.security.core.userdetails.User`的官方实现类

```java
public class UserEntity implements UserDetails {
 
    ...

    @Override
    public boolean equals(Object o) {
        return o instanceof UserEntity && this.username.equals(((UserEntity) o).username);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}

```
## 5 Spring Session解决集群会话问题

1. **引入相关依赖**
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
<dependency>
  <groupId>org.springframework.session</groupId>
  <artifactId>spring-session-data-redis</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
```yaml
spring:

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/spring?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: LY0115..

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    open-in-view: false
    properties:
      hibernate:
        enable_lazy_load_no_trans: true
        
  session:
    store-type: redis

  redis:
    host: 192.168.1.115
    port: 6379
```
```java
package cn.quguai.config;

@EnableRedisHttpSession
public class HttpSessionConfig {

    @Autowired
    private RedisIndexedSessionRepository sessionRepository;

    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(){
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }
}
```
> 注意这里并不需要前面的`HttpSessionEventPublisher`的Bean，因为这里Session管理全部交由SpringSession去管理，也就是Session的相关清理工作会自动帮助我们完成。

```java
package cn.quguai.config;

@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin().permitAll()
                .and().sessionManagement()
                .maximumSessions(1)// 最大用户数量
                .maxSessionsPreventsLogin(true);
                .sessionRegistry(sessionRegistry);
    }
}
```
