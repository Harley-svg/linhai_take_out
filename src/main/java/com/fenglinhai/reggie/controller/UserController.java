package com.fenglinhai.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fenglinhai.reggie.common.R;
import com.fenglinhai.reggie.entity.User;
import com.fenglinhai.reggie.service.UserService;
import com.fenglinhai.reggie.utils.SMSUtils;
import com.fenglinhai.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Harley
 * @create 2022-10-22 0:52
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("生成的验证码为{}",code);
            //调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("林海外卖","",phone,code);
            //需要将生成的验证码保存到Session中
//            session.setAttribute(phone,code);


            //将生成的验证码缓存到 redis中，并设置有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");


    }

    /**
     * 移动端用户登录
     * @param
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        //从session中获取保存的验证码
//        String codeInSession = (String) session.getAttribute(phone);

        //从redis中获取缓存的验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(phone);

        //进行验证码比对
        if(codeInRedis!=null&&codeInRedis.equals(code)){
            //如果比对成功，说明登陆成功
            //判断当前手机号是否为新用户，如果是新用户就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //如果用户登陆成功，删除redis缓存的验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
