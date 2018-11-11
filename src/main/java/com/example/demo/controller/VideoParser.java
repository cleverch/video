package com.example.demo.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yxx.code.result.RetResult;
import com.yxx.code.result.RetRetResponse;
import com.yxx.utli.HttpConnect;
import com.yxx.utli.URLUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Controller
public class VideoParser {
	@RequestMapping("/videoPaser")
	@ResponseBody
	public  RetResult<String> Parser(String txurl) {
		String url="";
		ArrayList<String> arrys=send(txurl);
		for(int i=0;i<arrys.size();i++) {
			String turl=arrys.get(i);
			String a=URLUtil.fromURL(turl).getParameter("url");
			//System.out.println(":"+a);
			url=sendapi(a);
			senduserlike(turl);
			System.out.println(url);
		}
		return RetRetResponse.makeOKRsp(url);
	}
	public  ArrayList<String> send(String id) {
		String url="http://y.mt2t.com/lines/getdata";
		String s=HttpConnect.sendPost(url,"url="+id+"&key=a0b923820dcc509a");
		JSONArray arry=JSONArray.fromObject(s);
		ArrayList<String> arrys=new ArrayList<>();
		for(int i=0;i<arry.size();i++) {
			JSONObject o=arry.getJSONObject(i);
			arrys.add(o.getString("Url"));
			System.out.println(o.getString("Url"));
			break;
		}
		
		return arrys;
		
	}
	public  String sendapi(String id) {
		String url="http://y3.mt2t.com:91/ifr/api";
		String s=HttpConnect.sendPost(url,"url="+id+"&type=&from=mt2t.com&device=&up=0");
		JSONObject o=JSONObject.fromObject(s);
		String url1=o.getString("url");
		if(!o.getString("ext").equals("mp4")) {
			return "";
		}
		//System.out.println("xx"+url1);
		return url1;
		
	}
	public  void senduserlike(String id) {
		String url="http://y.mt2t.com/lines/userlike";
		String s=HttpConnect.sendPost(url,"url="+id);
		System.out.println(s);
		
	}
	@RequestMapping("/getdata")
	@ResponseBody
	public  RetResult<String> getdata(String txurl) {
		String url1="http://y.mt2t.com/lines/getdata";
		String s=HttpConnect.sendPost(url1,"url="+txurl+"&key=a0b923820dcc509a");
		JSONArray arry=JSONArray.fromObject(s);
		ArrayList<String> arrys=new ArrayList<>();
		String url="";
		for(int i=0;i<arry.size();i++) {
			JSONObject o=arry.getJSONObject(i);
			arrys.add(o.getString("Url"));
			url=o.getString("Url");
			System.out.println(url+" --"+arry.size());
			break;
		}
		if(!url.equals("")) {
			return RetRetResponse.makeOKRsp(url);
		}
		return RetRetResponse.makeErrRsp("解析失败");
	}
	@RequestMapping("/api")
	@ResponseBody
	public  RetResult<String> api(String url,String up) {
		url=URLEncoder.encode(url);
		System.out.println("参数："+url);
		String url1="http://y3.mt2t.com:91/ifr/api";
		String s=HttpConnect.sendPost(url1,"url="+url+"&type=&from=mt2t.com&device=&up="+up);
		JSONObject o=JSONObject.fromObject(s);
		url=o.getString("url");
		System.out.println("xx"+url);
		if(url==null) {
			return RetRetResponse.makeErrRsp("解析失败");
		}
		return RetRetResponse.makeOKRsp(url);
		
	}
	
}
