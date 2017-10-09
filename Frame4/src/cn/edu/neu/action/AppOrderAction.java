package cn.edu.neu.action;

import cn.edu.neu.core.Constants;
import cn.edu.neu.core.common.Page;
import cn.edu.neu.model.Order;
import cn.edu.neu.model.OrderDetail;
import cn.edu.neu.model.User;
import cn.edu.neu.service.GoodsService;
import cn.edu.neu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/apporder")
public class AppOrderAction extends BaseAction{

    @Autowired
    OrderService orderService;
    GoodsService goodsService;
    @ResponseBody
    @RequestMapping("/getMyOrders")
    public Map<String,List<Order>> getMyOrders(HttpServletRequest httpServletRequest){
        Map<String,List<Order>> map=new HashMap<>();
        HttpSession session=httpServletRequest.getSession();
        User loginuser= (User)session.getAttribute(Constants.LOGIN_USER);

        Page<Order> orders=orderService.getMyOrders(loginuser.getUserId(),null);
        List<Order> myorders=orders.getList();
        map.put("ordersList",myorders);
        return map;
    }
    @ResponseBody
    @RequestMapping("/addOrder")
    public Map<String, String> addOrder(@RequestParam String address, HttpSession session,
                                        Map<String,String> m){

        Order order=new Order();
        order.setUserId(this.getLoginUserId());
        order.setOrderAddress(address);
        //order.setOrderPostalfee(Float.parseFloat(orderPostalfee));
        String[] goodsId=(String[])session.getAttribute("goodsId");
        String[] size=(String[])session.getAttribute("size");
        String[] color=(String[])session.getAttribute("color");
        String[] num=(String[])session.getAttribute("num");
        List<Map<String,Object>> goodsList=goodsService.getGoodsListByIds(goodsId,size,color,num);
        List<OrderDetail> orderDetails=new ArrayList<OrderDetail>();
        Map map=goodsList.get(0);
        float postalfee=(Float)map.get("goodsPostalfee");
        for(int i=0;i<goodsList.size();i++){
            OrderDetail od=new OrderDetail();
            od.setGoodsId(Integer.parseInt((String)(goodsList.get(i).get("goodsId"))));
            od.setOdetailName((String)goodsList.get(i).get("goodsName"));
            od.setOdetailPrice((Float)(goodsList.get(i).get("goodsDiscount")));
            od.setOdetailSize((String)goodsList.get(i).get("size"));
            od.setOdetailColor((String)goodsList.get(i).get("color"));
            od.setOdetailNum(Integer.parseInt(((String)goodsList.get(i).get("num"))));
            od.setOdetailPic((String)goodsList.get(i).get("goodsPic"));
            float temp=(Float)(goodsList.get(i).get("goodsPostalfee"));
            if(postalfee>temp)
                postalfee=temp;
            orderDetails.add(od);
        }
        order.setOrderPostalfee(postalfee);
        //System.out.println(orderDetails);
        try{

            orderService.addOrder(order,orderDetails);
            m.put("orderFlag",order.getOrderId()+"");
            return m;
        }
        catch(Exception e){
            e.printStackTrace();
            m.put("orderFlag","0");
            return m;
        }
    }
}
