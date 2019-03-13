

package com.hnradio.project.resource.common.cache;

import java.util.Set;




/**
 * 缓存操作接口
 */
public interface Cache {

	/**
	 * 列出所有的key
	 * @return
	 */
	public Set<String> getKeys();


	public Set<String> getKeys(String pattern);

	/**
	 * 检查给定key是否存在
	 *
	 * @param key
	 * @return
	 */
	public Boolean exists(String key);


	/**
	 * 移除给定的一个或多个key。如果key不存在，则忽略该命令。
	 *
	 * @param key
	 */
	public void del(String key);


	/**
	 * 简单的字符串设置
	 *
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);

	/**
	 *
	 * @param key
	 * @param value
	 * @param expiration
	 */
	public void set(String key, String value, Integer expiration);

	/**
	 * 返回key所关联的字符串值
	 *
	 * @param key
	 * @return
	 */
	public String get(String key);



	/**
	 * key seconds 为给定key设置生存时间。当key过期时，它会被自动删除。
	 *
	 * @param key
	 * @param expire
	 */
	public void expire(String key, int expire);


	/**
	 * 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
	 *
	 * @param key
	 * @param value
	 */
	public void append(String key, String value);




	/**
	 * 获取旧值返回新值，不存在返回nil
	 *
	 * @param key
	 * @param newValue
	 * @return 旧值
	 */
	public String getset(String key, String newValue);

	/**
	 * 分布锁
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setnx(String key, String value);


	/**
	 * 计数器
	 */
	public Long incrBy(String key, Long delta);



}
