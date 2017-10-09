package cn.edu.neu.action;

import cn.edu.neu.model.Category;
import cn.edu.neu.model.User;
import cn.edu.neu.service.CateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/appcate")
public class AppCateAction extends BaseAction{
    @Autowired
    CateService cateService;
    @ResponseBody
    @RequestMapping("/getAllCatesList")
    public Map<String,List<Category>> autoLogin(){
        Map<String, List<Category>> m=new HashMap<String, List<Category>>();
        List<Category> list=cateService.getAllCates();
        m.put("catesList",list);
        return m;
    }
}
