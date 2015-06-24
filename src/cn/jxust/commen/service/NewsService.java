package cn.jxust.commen.service;

import java.util.Date;

import cn.jxust.commen.model.News;
import cn.jxust.commen.model.User;

import com.jfinal.plugin.activerecord.Page;


public class NewsService
{

	public Page<News> findNewsPage(Integer pageNumber, Integer pageSize)
	{
		return News.dao.paginate(pageNumber, pageSize, "select news.*,user.fullname ", "from news,user where news.creater=user.id order by news.createtime desc");
	}

	public News findById(String id)
	{
		return News.dao.findById(id);
	}

	public void save(News news)
	{
		if(null==news.get("id"))
		{
			news.save();
		}
		else
		{
			news.update();
		}
	}
	
	public void update(News news, User user)
	{
		if(null==news.get("id"))
		{
			news.set("flag", 0);
			news.set("createtime", new Date());
			news.set("creater", user.get("id").toString());
		}
		save(news);
	}

	public void delete(String id)
	{
		News.dao.deleteById(id);
	}

	public void activation(String id)
	{
		News news=findById(id);
		news.set("flag", 1);
		save(news);
	}

	public void freeze(String id)
	{
		News news=findById(id);
		news.set("flag", 0);
		save(news);
	}

}
