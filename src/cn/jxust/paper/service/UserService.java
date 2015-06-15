package cn.jxust.paper.service;

import java.util.List;

import cn.jxust.commen.model.User;
import cn.jxust.util.MD5;

import com.jfinal.plugin.activerecord.Page;

public class UserService
{
	/**
	 * 查询学生列表
	 */
	public List<User> findStudentList()
	{
		return User.dao.find("select * from user where role=1 order by id");
	}

	/**
	 * 查询教师列表
	 */
	public List<User> findTeacherList()
	{
		return User.dao.find("select * from user where role=2 order by id");
	}

	/**
	 * 查询学生页面
	 */
	public Page<User> findStudentPage(Integer pageNumber,Integer pageSize)
	{
		return User.dao.paginate(pageNumber, pageSize, "select * ","from user where role=1 order by id");
	}

	/**
	 * 查询教师页面
	 */
	public Page<User> findTeacherPage(Integer pageNumber, Integer pageSize)
	{
		return User.dao.paginate(pageNumber, pageSize, "select * ","from user where role=2 order by id");
	}

	/**
	 * 查询单个用户
	 */
	public User findById(String id)
	{
		return User.dao.findById(id);
	}

	/**
	 * 加工处理保存用户信息
	 */
	public void update(User user)
	{
		if(null==user.get("id"))
		{
			user.set("password", MD5.getMD5Str2((String)user.get("password")));//对用户密码加密
			user.set("flag", "1");
			save(user);
		}
		else
		{
			User user_old=findById(user.get("id").toString());
			if(!user.get("password").equals(user_old.get("password")))
			{
				user.set("password", MD5.getMD5Str2((String)user.get("password")));//对用户密码加密
			}
			user.set("flag", "1");
			save(user);
		}
		
	}
	
	/**
	 * 封装保存操作
	 */
	public void save(User user)
	{
		if(null==user.get("id"))
		{
			user.save();
		}
		else
		{
			user.update();
		}
	}

	/**
	 * 删除单个用户
	 */
	public void delete(String id)
	{
		User.dao.deleteById(id);
	}

	/**
	 * 激活账号
	 */
	public void activation(String id)
	{
		User user=findById(id);
		user.set("flag", 1);
		save(user);
	}

	/**
	 * 冻结账号
	 */
	public void freeze(String id)
	{
		User user=findById(id);
		user.set("flag", 0);
		save(user);
	}
	

}
