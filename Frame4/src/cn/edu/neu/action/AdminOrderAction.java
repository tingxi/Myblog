package cn.edu.neu.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.neu.core.Constants;
import cn.edu.neu.core.common.Page;
import cn.edu.neu.model.Order;
import cn.edu.neu.service.OrderService;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderAction extends BaseAction{
	
	@Autowired
	OrderService orderService;

	@RequestMapping("/getAdminOrders")
	public String getAdminOrders(@RequestParam(required=false) String orderCode,@RequestParam(required=false) String userName,
	@RequestParam(required=false) String orderStatus,@RequestParam(required=false) String startDate,@RequestParam(required=false) String endDate,
	@RequestParam(required=false) String sort,Map<String,Object> m){
		Page<Order> orders=orderService.getAdminOrders(orderCode,userName,orderStatus,startDate,endDate,sort);
		m.put("orders", orders);
		return "/admin/order/orderList";
	}
	
	@RequestMapping("/getOrderDetailById")
	public String getOrderDetailById(@RequestParam String orderId,Map<String,Order> m){
		Order orderDetail=orderService.getOrderDetailById(orderId);
		m.put("orderDetail", orderDetail);
		return "/admin/order/orderDetail";
	}
	
	@ResponseBody
	@RequestMapping("/sendGoods")
	public Map<String,String> sendGoods(@RequestParam String orderId,@RequestParam int status,@RequestParam String orderPostname,@RequestParam String orderPostcode){
		Map<String,String> m=new HashMap<String,String>();
		try{
			if(orderService.getOrderStatusById(orderId)==Constants.ORDER_PAID){
				orderService.updateOrderPostInfo(orderId,orderPostname,orderPostcode,status);
				m.put("handle", "success");
			}else{
				m.put("handle", "failure");
			}
		}
		catch(Exception e){
			e.printStackTrace();		
			m.put("handle", "exception");
		}
		return m;
	}
	
	@ResponseBody
	@RequestMapping("/handleOrderStatus")
	public Map<String,String> handleOrderStatus(@RequestParam String orderId,@RequestParam int status){
		Map<String,String> m=new HashMap<String,String>();
		try{
			
			switch(status){
			case 3://同意退款
				//System.out.println("----"+this.getServMgr().getOrderService().getOrderStatusById(orderId));
				if(orderService.getOrderStatusById(orderId)==Constants.ORDER_ASKREFUND){
					orderService.changeOrderStatus(orderId,status);
					m.put("handle", "success");
				}else{
					m.put("handle", "failure");
				}
				break;
			
			case 7://同意退货
				if(orderService.getOrderStatusById(orderId)==Constants.ORDER_ASKRETURN){
					orderService.changeOrderStatus(orderId,status);
					m.put("handle", "success");
				}else{
					m.put("handle", "failure");
				}	
				break;
			case 8://确认收到退货
				if(orderService.getOrderStatusById(orderId)==Constants.ORDER_RETURNING){
					orderService.changeOrderStatus(orderId,status);
					m.put("handle", "success");
				}else{
					m.put("handle", "failure");
				}	
				break;	
			case 9://取消订单
				if(orderService.getOrderStatusById(orderId)==Constants.ORDER_WAITING){
					orderService.changeOrderStatus(orderId,status);
					m.put("handle", "success");
				}else{
					m.put("handle", "failure");
				}	
				break;
			default:
				m.put("handle", "failure");
				break;
			}		
		}
		catch(Exception e){
			e.printStackTrace();		
			m.put("handle", "exception");
		}
		return m;
	}
	
	@RequestMapping("/delOrder")
	public String delOrder(@RequestParam String orderId){
		try{
			int status=orderService.getOrderStatusById(orderId);
			if(status==Constants.ORDER_SUCCEED || status==Constants.ORDER_CLOSED 
				|| status==Constants.ORDER_REFUNDSUCCEED || status==Constants.ORDER_RETURNSUCEED){
				orderService.delOrder(orderId);
				//return "forward:/order/getMyOrders";
				return "forward:"+getReferUrl();
			}else{
				this.addMessage("抱歉，目前的订单状态不允许删除订单");
				this.addRedirURL("返回", "@back");
				return EXECUTE_RESULT;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			this.addMessage("取消订单失败");
			this.addRedirURL("返回", "@back");
			return EXECUTE_RESULT;
		}
	}
	
	@RequestMapping("/delOrderByIds")
	public String delOrderByIds(@RequestParam String[] orderIds){
		try{
			Integer[] status=orderService.getOrderStatusByIds(orderIds);
			boolean flag=true;
			for(int i=0;i<status.length;i++){
				if(status[i]!=3&&status[i]!=5&&status[i]!=8&&status[i]!=9){
					flag=false;
				}
			}
			if(flag){
				orderService.delOrderByIds(orderIds);
				this.addMessage("订单删除成功");
				this.addRedirURL("返回",getReferUrl());
			}
			else{
				this.addMessage("只有状态为交易成功、交易关闭、退款成功、退货成功的订单允许删除，当前批量删除操作失败");
				this.addRedirURL("返回","@back");
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			this.addMessage("订单删除失败");
			this.addRedirURL("返回","@back");
		}
		return EXECUTE_RESULT;
	}
}
