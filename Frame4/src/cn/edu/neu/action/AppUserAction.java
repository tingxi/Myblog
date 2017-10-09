package cn.edu.neu.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.neu.core.Constants;
import cn.edu.neu.core.util.Md5Util;
import cn.edu.neu.model.User;
import cn.edu.neu.service.UserService;

@Controller
@RequestMapping("/appuser")
public class AppUserAction extends BaseAction{
	@Autowired
	UserService userService;
	@ResponseBody
	@RequestMapping("/login")
	public Map<String, String> login(User user,HttpSession session){
		System.out.println("login："+user);
		User dbUser=userService.checkUser(user);
		Map<String, String> m=new HashMap<String, String>();
		if(dbUser!=null){
			session.setAttribute(Constants.LOGIN_USER, dbUser);
			m.put("loginFlag", "1");
			m.put("msg", "");
		}
		else{
			m.put("loginFlag", "0");
			m.put("msg", "用户名或密码错误");
		}
		return m;
	}
	@ResponseBody
	@RequestMapping("/logout")
	public Map<String, String> logout(User user,HttpServletRequest request) {
		HttpSession session=request.getSession(false);
		Map<String,String> m=new HashMap<String,String>();
		if(session==null){//登出失败--已经处于退出登录状态
		m.put("loginFlag", "0");
	}
	else{//登出成功 
		session.invalidate();
		m.put("loginFlag", "1");
	}
	return m;
}
	@ResponseBody
	@RequestMapping("/reg")
	public Map<String,String> reg(User user)  {	
		boolean f=userService.regUser(user);
		Map<String,String> m=new HashMap<String,String>();
		if(f){//注册成功 
			m.put("regFlag", "1");
			m.put("msg", "");
		}
		else{//注册失败
			m.put("regFlag", "0");
			m.put("msg", "注册失败");			
		}
		return m;
	}
	    @ResponseBody
		@RequestMapping("/autoLogin")
		public Map<String,String> autoLogin(String userName,
				String authority,HttpSession session) {
			System.out.println("username:"+userName+",authority:"+authority);
			User user=userService.getUser(userName);
			System.out.println("user:"+user);
			String serverAuth=Md5Util.getMD5Str(userName+","+
						Md5Util.getMD5Str(user.getUserPass()));
			Map<String,String> m=new HashMap<String,String>();
			if(authority.equals(serverAuth)){//自动登录成功
				session.setAttribute(Constants.LOGIN_USER, user);
				m.put("autoLoginFlag", "1");
				m.put("msg", "");
			}
			else{//自动登录失败
				m.put("autoLoginFlag","0");
				m.put("msg", "自动登录失败，请在登录页面进行登录");
			}
			return m;
		}
}
