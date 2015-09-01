package net.bitacademy.java72.control.json2;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.bitacademy.java72.domain.Member;
import net.bitacademy.java72.service.MemberService;

@Controller
@RequestMapping("/json2/auth")
public class AuthController {
  @Autowired MemberService memberService;
  
  @RequestMapping(value="/login", 
      method=RequestMethod.POST)
  
  @ResponseBody
  public String login(
      String email, 
      String password,
      String saveEmail,
      HttpServletResponse response,
      HttpSession session) throws Exception {
    
    System.out.println("test01");
    
    HashMap<String, Object> resultMap = new HashMap<>();
    

    if (saveEmail != null) {
      Cookie cookie = new Cookie("email", email);
      cookie.setMaxAge(60 * 60 * 24);
      response.addCookie(cookie);
    } else { 
      // 기억하기를 체크하지 않았다면, 쿠키를 무효화 시킨다.
      Cookie cookie = new Cookie("email", "");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }

    Member member = memberService.getUser(email, password);

    if (member == null) {
      session.invalidate();
      
      resultMap.put("result", "failure");
     
      
    } else {
      session.setAttribute("member", member);

      String refererUrl = 
          (String)session.getAttribute("refererUrl");
      if (refererUrl == null) {
        resultMap.put("refererUrl", refererUrl);
      } else {
        resultMap.put("result", "success");
      }
    }
    return new Gson().toJson(resultMap);
  }
  
  @RequestMapping("/logout")
  @ResponseBody
  public String logout(HttpSession session) {
    
    HashMap<String, Object> resultMap = new HashMap<>();
    session.invalidate(); 
    resultMap.put("result", "success");
    return new Gson().toJson(resultMap);
  }
}






