package com.gl.handler;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gl.model.User;
import com.gl.service.UserService;
import com.gl.token.UserToken;

public class Authentication implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                            Object object) throws Exception {
      // 从 http 请求头中取出 token
      String token = httpServletRequest.getHeader("token");
      // 如果不是映射到方法直接通过
      if (!(object instanceof HandlerMethod)) {
         return true;
      }
      // 获取请求的URL
      String url = httpServletRequest.getRequestURI();
      // 登录请求放行，不拦截
      if (url.indexOf("/api/login") >= 0) {
         return true;
      }
      HandlerMethod handlerMethod = (HandlerMethod) object;
      Method method = handlerMethod.getMethod();

      // 检查有没有需要用户权限的注解
      if (method.isAnnotationPresent(UserToken.class)) {
         UserToken userToken = method.getAnnotation(UserToken.class);
         if (userToken.required()) {
            // 验证session
            HttpSession session = httpServletRequest.getSession();
            User user = (User) session.getAttribute(UserService.USERNAME);
            if (token == null) {
               httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"用户登录验证不正确");
               return false;
            }

//            // 执行认证
//            if (token == null) {
//               httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"用户登录验证不正确");
//               return false;
//            }
            // 验证 token
            try {
               JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
               jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
               httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"用户登录验证不正确");
               return false;
            }
            return true;

         }
         HttpSession session = httpServletRequest.getSession();
         User user = (User) session.getAttribute(UserService.USERNAME);
         JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
         try {
            // Token有效期检查
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Date expirationDate = decodedJWT.getExpiresAt();
            Date now = new Date();
            if (now.after(expirationDate)) {
               httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token已过期");
               return false;
            }

            // Token的Audience检查
            String audience = decodedJWT.getAudience().get(0);
            if (!"admin".equals(audience)) {
               httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的Token观众");
               return false;
            }
         } catch (JWTVerificationException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "用户登录验证不正确");
            return false;
         }


      }
      return true;
   }

   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) throws Exception {

   }

   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
           throws Exception {

   }
}