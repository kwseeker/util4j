package net.jueb.tools.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import net.jueb.serializable.nobject.base.P;

/**
 * 文件-字节数组-io流之间的转换
 * @author juebanlin
 *
 */
public class FileStreamBytes {
	/**
	 * 截获输入流数据并保存数据到文件
	 * 不关闭输入流
	 * @param in 待截获的输入流
	 * @param file 将输入流中的数据保存到文件
	 * @return 返回输入流
	 * @throws IOException
	 */
	public InputStream saveFileAndReturnStream(InputStream in,File file) throws IOException
	{
		byte[] data=inputStreamToByteData(in);//获取数据
		InputStream filedata=new MyInputStream(data);//根据data建立一个输入流
		saveFileByInputStream(filedata, file);//将数据写到文件
		InputStream input=byteArrayToInputStream(data);//根据数据还原输入流；因为输入流读取完了，就没了
		return input;
	}
	/**
	 * 将输入流的数据写到文件
	 * 不关闭输入流
	 * @param in
	 * @param file
	 * @throws IOException
	 */
	public void saveFileByInputStream(InputStream in,File file) throws IOException
	{
		FileOutputStream fos=new FileOutputStream(file);//定义一个输出到文件的流
		BufferedOutputStream bos=new BufferedOutputStream(fos);//包装该流
		int i=0;
		while((i=in.read())!=-1)
		{			
			bos.write(i);//每次从输入流读取一个字节到缓冲区
		}
		bos.flush();//写出数据
		bos.close();
	}
	
	/**
	 * 根据字节数组保存到文件
	 * @param data
	 * @param file
	 * @throws IOException
	 */
	public void byteArrayToFile(byte[] data,File file) throws IOException
	{
		FileOutputStream fos=new FileOutputStream(file);//定义一个输出到文件的流
		BufferedOutputStream bos=new BufferedOutputStream(fos);//包装该流
		bos.write(data);
//		for(int i:data)
//		{
//			bos.write(i);//每次从输入流读取一个字节到缓冲区
//		}
		bos.flush();//写出数据
		bos.close();
	}
	/**
	 * 根据一个byte数组返回一个输入流
	 * @param data
	 * @return
	 */
	public MyInputStream byteArrayToInputStream(byte[] data)
	{
		System.out.println("MyInputStream被创建,数组长度:"+data.length);
		return new MyInputStream(data);
	}
	/**
	 * 获取输入流的数据，返回该数据的byte数组
	 * 不关闭输入流
	 * 使用ByteArrayOutputStream()实现
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	public byte[] inputStreamToByteData(InputStream in) throws IOException
	{
		BufferedInputStream bis=new BufferedInputStream(in);//定义一个缓冲输入流
		ByteArrayOutputStream data=new ByteArrayOutputStream();//定义一个内存输出流
		int i=-1;
		while((i=bis.read())!=-1)//如果没读取完，在继续
		{
			data.write(i);//保存到内存数组
		}
		data.flush();
		data.close();
		return data.toByteArray();
	}
	/**
	 * 根据文件拿到此文件的byte数组
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public byte[] getByteData(File file) throws IOException
	{
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bis=new BufferedInputStream(fis);
		ByteArrayOutputStream data=new ByteArrayOutputStream();//定义一个内存输出流
		int i=-1;
		while((i=bis.read())!=-1)//如果没读取完，在继续
		{
			data.write(i);//保存到内存数组
		}
		data.flush();
		data.close();
		bis.close();
		return data.toByteArray();
	}
	/**
	 * 读取bytes中以p位置开始的length个长度的数据
	 * @param bytes
	 * @param p [0,……]
	 * @param length
	 * @return 读取错误返回null
	 */
	public byte[] readByteArrayByLenght(byte[] bytes,int p,int length)
	{
		if(p+length<=bytes.length&&p>=0&&length>=0)
		{
			byte[] v=new byte[length];
			for(int i=0;i<length;i++)
			{
				v[i]=bytes[p];//读取移动后的字节内容
				p++;
			}
			return v;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 判断从P位置开始可以读取length个长度的数据吗
	 * @param bytes
	 * @param p
	 * @param length
	 * @return
	 */
	public boolean canReadLength(byte[] bytes,int p,int length)
	{
		if(bytes==null)
		{
			return false;
		}
		if(p+length<=bytes.length&&p>=0&&length>=0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean equalsByteArray(byte[] array1,byte[] array2)
	{
		//当都为null时
		if(array1==array2)
		{
			return true;
		}
		if(array1==null|array2==null)
		{
			return false;
		}
		if(array1.length!=array2.length)
		{
			return false;
		}
		if(Arrays.toString(array1).equals(Arrays.toString(array2)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 读取bytes中以p位置开始的length个长度的数据
	 * p的位置移动到读取的数据之后
	 * @param bytes
	 * @param p [0,……]
	 * @param length 如果为0，P位置不变
	 * @return 读取错误返回null
	 */
	protected static byte[] readByteArrayByLenght(byte[] bytes,P p,int length)
	{
		if(p.value()+length<=bytes.length&&p.value()>=0&&length>=0)
		{
			byte[] v=new byte[length];
			for(int i=0;i<length;i++)
			{
				v[i]=bytes[p.value()];//读取移动后的字节内容
				p.move(1);
			}
			return v;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 判断从P位置开始可以读取length个长度的数据吗
	 * p位置不变
	 * @param bytes
	 * @param p
	 * @param length
	 * @return
	 */
	protected static boolean canReadLength(byte[] bytes,P p,int length)
	{
		if(bytes==null)
		{
			return false;
		}
		int i=p.value();
		if(i+length<=bytes.length&&i>=0&&length>=0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * 从P位置开始读取以endArray数组结尾的数据.成功后P移动到endArray后面
	 * 返回[p,endArray]区间的数据
	 * @param bytes
	 * @param p
	 * @param endArray 不能为null或者空
	 * @return
	 */
	public  byte[] readByteArrayByEndArray(byte[] bytes,P p,byte[] endArray)
	{
		if(bytes==null|p==null|endArray==null|endArray.length<=0|p.value()>=bytes.length)
		{
			System.out.println("endArray为null或空");
			return null;
		}
		ArrayList<Byte> value=new ArrayList<Byte>();
		ArrayList<Byte> end=new ArrayList<Byte>();
		byte[] tmp;
		if(!canReadLength(bytes, p, endArray.length))
		{//先读取结尾个数组
			return null;
		}else
		{
			tmp=readByteArrayByLenght(bytes, p,endArray.length);
			if(equalsByteArray(tmp,endArray))
			{//如果一开始就读取到结尾数组，则返回
				return tmp;
			}else
			{//如果没有，就位移判断
				for(int t1=0;t1<tmp.length;t1++)
				{//先把之前读取的放入vlaue
					value.add(tmp[t1]);
					end.add(tmp[t1]);
				}
				while(canReadLength(bytes, p, 1))
				{//每次读一个
					byte[] i=readByteArrayByLenght(bytes, p, 1);
					value.add(i[0]);//读取后放入value
					//位移tmp
					end.remove(0);
					//加入新读取的
					end.add(i[0]);
					//判断是否相等-转换为byte[]
					byte[] t2=new byte[end.size()];
					for(int t3=0;t3<end.size();t3++)
					{
						t2[t3]=end.get(t3);
					}
					//判断是否相等-判断
					if(equalsByteArray(t2, endArray))
					{//如果相等，则返回value数据
						byte[] t4=new byte[value.size()];
						for(int t5=0;t5<value.size();t5++)
						{//导出数据
							t4[t5]=value.get(t5);	
						}
						return t4;
					}//如果不相等，则读取下一个
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		FileStreamBytes fsb=new FileStreamBytes();
		byte[] a={1,2,3,4};
		byte[] b={2,3};
		byte[] c=fsb.readByteArrayByLenght(a, 1,2);
		byte[] d=null;
		byte[] e=null;
		byte[] f={2,3};
		System.out.println(fsb.equalsByteArray(b,c));
	}
}
