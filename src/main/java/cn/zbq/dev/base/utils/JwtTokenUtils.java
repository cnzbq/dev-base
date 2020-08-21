package cn.zbq.dev.base.utils;


import cn.zbq.dev.base.propites.AuthorizationProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * jwt工具类
 *
 * @author zbq
 * @date 2020/8/19
 */
@Slf4j
public class JwtTokenUtils {
    private static final Algorithm ALGORITHM;
    private static final JWTVerifier JWTVERIFIER;
    public static final String ISSUER;
    public static final String USER_ID_KEY;
    private static final String JWT_SECRET;
    private final static int EFFECTIVE_TIME;

    /**
     * 创建token
     *
     * @param userId 用户id
     * @return token
     */
    public static String creatJwtToken(Long userId) {
        try {
            JWTCreator.Builder builder = JWT.create()
                    .withSubject("token")
                    .withIssuer(ISSUER)
                    .withClaim(USER_ID_KEY, userId)
                    .withExpiresAt(DateUtils.addSeconds(new Date(), EFFECTIVE_TIME));

            return builder.sign(ALGORITHM);
        } catch (IllegalArgumentException | JWTCreationException e) {
            log.error("生成token失败:", e);
            throw new ApiException("生成token失败");
        }
    }

    /**
     * 验证token是否有效
     *
     * @param token token
     * @throws ApiException 当token验证失败时抛出
     */
    public static void verifierToken(String token) {
        decodedJWT(token);
    }


    private static DecodedJWT decodedJWT(String token) {
        try {
            return JWTVERIFIER.verify(token);
        } catch (JWTVerificationException e) {
            if (log.isDebugEnabled()) {
                log.debug("token验证失败:", e);
            }
            throw new ApiException("token已失效");
        }
    }

    /**
     * get 一个 Boolean 类型的值
     *
     * @return /
     */
    public static Optional<Boolean> getBoolean(String token, String name) {
        return Optional.ofNullable(getClaim(token, name).asBoolean());
    }

    /**
     * get 一个 int 类型的值
     *
     * @return /
     */
    public static Optional<Integer> getInt(String token, String name) {
        return Optional.ofNullable(getClaim(token, name).asInt());
    }

    /**
     * get 一个 Long 类型的值
     *
     * @return /
     */
    public static Optional<Long> getLong(String token, String name) {
        return Optional.ofNullable(getClaim(token, name).asLong());
    }

    /**
     * get 一个 Double 类型的值
     *
     * @return /
     */
    public static Optional<Double> getDouble(String token, String name) {
        return Optional.ofNullable(getClaim(token, name).asDouble());
    }

    /**
     * get 一个 String 类型的值
     *
     * @return /
     */
    public static Optional<String> getString(String token, String name) {
        return Optional.ofNullable(getClaim(token, name).asString());
    }

    /**
     * get 一个 Date 类型的值
     *
     * @return /
     */
    public static Optional<Date> getDate(String token, String name) {
        return Optional.ofNullable(getClaim(token, name).asDate());
    }

    /**
     * get 一个 map 类型的值
     *
     * @return /
     */
    public static Optional<Map<String, Object>> getMap(String token, String name) {
        try {
            return Optional.ofNullable(getClaim(token, name).asMap());
        } catch (JWTDecodeException e) {
            log.error("the value can't be converted to a Map.", e);
            return Optional.empty();
        }
    }

    /**
     * get 一个 指定 class 类信息的值
     *
     * @param <T>    类型
     * @param tClazz 对应类型的类
     * @return /
     */
    public static <T> Optional<T[]> getArray(Class<T> tClazz, String token, String name) {
        try {
            return Optional.ofNullable(getClaim(token, name).asArray(tClazz));
        } catch (JWTDecodeException e) {
            log.error("the values inside the Array can't be converted to a class {}.", tClazz, e);
            return Optional.empty();
        }
    }

    /**
     * get 一个指定元素类型的 list
     *
     * @param <T>    元素类型
     * @param tClazz 对应类型的类
     * @return /
     */
    public static <T> Optional<List<T>> getList(Class<T> tClazz, String token, String name) {
        try {
            return Optional.ofNullable(getClaim(token, name).asList(tClazz));
        } catch (JWTDecodeException e) {
            log.error("the values inside the List can't be converted to a class {}", tClazz, e);
            return Optional.empty();
        }
    }

    private static Claim getClaim(String token, String key) {
        return decodedJWT(token).getClaim(key);
    }


    public static JwtTokenBuilder builder() {
        return new JwtTokenBuilder();
    }

    /**
     * 构建一个token
     */
    public static class JwtTokenBuilder {
        private final JWTCreator.Builder builder;

        JwtTokenBuilder() {
            builder = JWT.create()
                    .withSubject("token")
                    .withIssuer(ISSUER)
                    .withExpiresAt(DateUtils.addSeconds(new Date(), EFFECTIVE_TIME));
        }

        /**
         * 添加一些信息体到token中
         *
         * @param consumer 实现一个消费接口，通过 {@link JWTCreator.Builder#withClaim} 方法添加想要存入token中的信息
         * @return /
         */
        public JwtTokenBuilder map(Consumer<JWTCreator.Builder> consumer) {
            consumer.accept(builder);
            return this;
        }

        /**
         * 添加一个Boolean类型的值
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Boolean value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个Integer类型的值
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Integer value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个Long类型的值
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Long value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个Double类型的值
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Double value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个String类型的值
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, String value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个Date类型的值
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Date value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个String数组
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, String[] value) throws IllegalArgumentException {
            builder.withArrayClaim(name, value);
            return this;
        }

        /**
         * 添加一个Integer数组
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Integer[] value) throws IllegalArgumentException {
            builder.withArrayClaim(name, value);
            return this;
        }

        /**
         * 添加一个Long数组
         *
         * @throws IllegalArgumentException name 为空时抛出
         */
        public JwtTokenBuilder addClaim(String name, Long[] value) throws IllegalArgumentException {
            builder.withArrayClaim(name, value);
            return this;
        }

        /**
         * 添加一个map
         * <p>
         * 接受的类型有 {@linkplain Map} / {@linkplain List}
         * 它们中的类型为以下的基本类型
         * {@linkplain Boolean}, {@linkplain Integer}, {@linkplain Long}, {@linkplain Double},
         * {@linkplain String} / {@linkplain Date}. {@linkplain Map}中key和value都不可以null.
         * {@linkplain List}不能包含null.
         *
         * @throws IllegalArgumentException 名字为空或者当map不满足上诉条件.
         */
        public JwtTokenBuilder addClaim(String name, Map<String, ?> value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        /**
         * 添加一个list
         * <p>
         * 接受的类型有 {@linkplain Map} 和 {@linkplain List}
         * 它们中的类型应为以下的基本类型
         * {@linkplain Boolean}, {@linkplain Integer}, {@linkplain Long}, {@linkplain Double},
         * {@linkplain String} 和 {@linkplain Date}. {@linkplain Map}要保证key和value不为null.
         * {@linkplain List}不含null.
         *
         * @throws IllegalArgumentException 名字为空或者当list不满足上诉条件.
         */

        public JwtTokenBuilder addClaim(String name, List<?> value) throws IllegalArgumentException {
            builder.withClaim(name, value);
            return this;
        }

        public String build() {
            try {
                return builder.sign(ALGORITHM);
            } catch (IllegalArgumentException | JWTCreationException e) {
                log.error("生成token失败:", e);
                throw new ApiException("生成token失败");
            }
        }
    }

    static {
        AuthorizationProperties properties = SpringBeanUtils.getBean(AuthorizationProperties.class);
        String jwtSecret = properties.getJwtSecret();
        Integer effectiveTime = properties.getEffectiveTime();
        String issuer = properties.getIssuer();
        String userIdKey = properties.getUserIdKey();

        if (StringUtils.isEmpty(jwtSecret)) {
            log.error("Please check your config: token.jwt-secret, the value must not null");
        }
        if (Objects.isNull(effectiveTime)) {
            log.error("Please check your config: token.effective-time, the value must not null");
        }
        if (StringUtils.isEmpty(issuer)) {
            log.error("Please check your config: token.issuer, the value must not null");
        }
        if (StringUtils.isEmpty(userIdKey)) {
            log.error("Please check your config: token.user-id-key, the value must not null");
        }
        JWT_SECRET = jwtSecret;
        USER_ID_KEY = userIdKey;
        ISSUER = issuer;
        EFFECTIVE_TIME = effectiveTime;

        try {
            ALGORITHM = Algorithm.HMAC256(JWT_SECRET);
        } catch (IllegalArgumentException e) {
            log.error("jwt密钥算法实例创建失败:", e);
            throw e;
        }
        JWTVERIFIER = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
    }
}
