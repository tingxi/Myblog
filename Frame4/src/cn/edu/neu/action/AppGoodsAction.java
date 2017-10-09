package cn.edu.neu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.neu.core.common.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.neu.model.Goods;
import cn.edu.neu.service.GoodsService;

@Controller
@RequestMapping("/appgoods")
public class AppGoodsAction extends BaseAction{
	@Autowired
	GoodsService goodsService;

	@ResponseBody
	@RequestMapping("/getNewGoods")
	/*public List<Goods> getNewGoods(){
		List<Goods> newGoods=goodsService.getNewGoods();
		Map<String,List<Goods>> m=new HashMap<String,List<Goods>>();
		m.put("newGoods", newGoods);
		return newGoods;
	}*/
	public Map<String,List<Goods>> getNewGoods(){
		List<Goods> newGoods=goodsService.getNewGoods();
		Map<String,List<Goods>> m=new HashMap<String,List<Goods>>();
		m.put("newGoods", newGoods);
		return m;
	}
	
	@ResponseBody
	@RequestMapping("/getSalesGoods")
	public  Map<String,List<Goods>> getSalesGoods(){
		List<Goods> salesGoods=goodsService.getSalesGoods();
		Map<String,List<Goods>> m=new HashMap<String,List<Goods>>();
		m.put("salesGoods", salesGoods);
		return m;
	}

	@ResponseBody
	@RequestMapping("/getGoodsByCate")
	public Map<String,List<Goods>> getGoodsByCate(String cateId){
		Map<String, List<Goods>> m=new HashMap<String, List<Goods>>();
		List<Goods> goods=new ArrayList<>();
		String[] ids=goodsService.getGoodsIdsByCateId(cateId);
		for (String id:
				ids) {
			goods.add(goodsService.getGoodsById(id));
		}
		m.put("goodsList",goods);
		return m;
	}
//
//	@ResponseBody
//	@RequestMapping("/searchGoods")
//	public Map<String,List<Goods>> searchGoods(String keyword) {
//		List<Goods> searchGoods=goodsService.searchGoodsList(keyword);
//		Map<String,List<Goods>> m=new HashMap<String,List<Goods>>();
//		m.put("goodsList", searchGoods);
//		return m;
//	}
	
	@ResponseBody
	@RequestMapping("/getGoodsDetailById")
	public Map<String,Goods> getGoodsDetailById(String goodsId){
		Goods goodsDetail=goodsService.getGoodsDetailById(goodsId);
		Goods goodsColors=goodsService.getGoodsColorsById(goodsId);
		Goods goodsSizes=goodsService.getGoodsSizesById(goodsId);
		
		Map<String,Goods> m=new HashMap<String,Goods>();
		m.put("goodsDetail", goodsDetail);
		m.put("goodsColors", goodsColors);
		m.put("goodsSizes", goodsSizes);
		System.out.println("----"+goodsDetail);
		return m;
	}
	@ResponseBody
	@RequestMapping("/searchGoods")
	public Map<String,List<Goods>> searchGoods(String goodsId){
		Map<String,List<Goods>> m=new HashMap<String,List<Goods>>();
		Page<Goods> page;
		page=goodsService.searchGoods(goodsId,null);
		List<Goods> goods=page.getList();
		m.put("goodsList",goods);
		return m;
	}
}




