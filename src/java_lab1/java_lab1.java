/* 1: ֧�ּ��źͿո�
 * 2��֧�ֶ����ĸ֮�䣬��ĸ����֮��ʡ�Գ˺�
 * 3��֧�ָ�����
 * 4��֧�������﷨���
 * 5��֧��ͬʱ��ֵ�������
 * 6��֧�ָ��������ݣ�֧�����������Ƕ�׺���ݣ�֧�ָߴΣ�ʮλ�����ϣ�������
 * 7: ֧�ֱ��ʽ�����﷨���
 * */

package java_lab1;

import java.util.regex.*;

public class java_lab1 
{
	final static double EPS=1e-6;//�ж�ĳdouble�Ƿ�Ϊ0��
	final static int MAX_NUMEBR = 1000;//���֧������
	final static int BIG_NUMEBR = 30;//������
	final static int CHECK_NUMEBR = 30;//������	
	static int exponent_matrix[][];//�洢�η�����
	static double coefficient_array[];//�洢ϵ��

	public static boolean zero(double a)
	{
		return (a<EPS)&&(a>-EPS);
	}
	
	public static boolean syntax_check(String a)//���ʽ�����﷨���
	{
		if(!a.matches("^[0-9a-zA-Z().+*\\-\\^]+$"))//�����ַ����
		{
			return false;
		}
		
		Pattern[] check =new Pattern[CHECK_NUMEBR];
		int check_index=0;
		check[check_index++]=Pattern.compile("^.*\\.[^0-9].*$");//С���������Ĳ������ֵ����
		check[check_index++]=Pattern.compile("^.*\\^[0-9]*\\.[0-9]*$");//С����ǰ�治�����ֵ����
		
		check[check_index++]=Pattern.compile("^.*\\^[0-9]*[.\\^].*$");//^��С�������
		check[check_index++]=Pattern.compile("^.*\\^[^0-9].*$");//^�������ֵ����
		
		for(int i=0;i<check_index;i++)
		{
			Matcher test=check[i].matcher(a);
			if(test.find())
			{
				return false;
			}
		}

		//���ŵļ��
		int left_parentheses=0;
		int right_parentheses=0;
		for(int i=0;i<a.length();i++)
		{
			if(a.charAt(i)=='(')
			{
				left_parentheses++;
			}
			if(a.charAt(i)==')')
			{
				right_parentheses++;
			}
			if(left_parentheses<right_parentheses)
			{
				return false;
			}
		}
		if(left_parentheses!=right_parentheses)
		{
			return false;
		}//�����ŵ��б�
		
		return true;
	}
	
	public static String initialize(String a)//���ַ�������Ԥ����
	{
		
		a=a.replaceAll("[\\s]","");//�ȴ���ո�

		a=a.replaceAll("([^0-9])\\.", "$10.");//С����ǰ���0�Ĳ���
		
		a=a.replaceAll("([a-zA-Z0-9])([A-Za-z])", "$1*$2");//��ĸ֮��ĳ˺�
		a=a.replaceAll("([a-zA-Z0-9])([A-Za-z])", "$1*$2");//����������ʽ�����ԣ��˴�Ҫ����
		
		a=a.replaceAll("([a-zA-Z])([0-9])","$1*$2" );//a4������Ϊa*4
		
		a=a.replaceAll("([0-9a-zA-Z])(\\()", "$1*$2");//����/��ĸ������

		a=a.replaceAll("(\\))([A-Za-z0-9])", "$1*$2");//���ų���ĸ/����
		//ע������һ��Ҫѡ���̰��ģʽ
		a=a.replaceAll("\\)\\(",")*(");//���ų�����
	    System.out.println(a);		
		a=a.replace("-","+%*");//��%����-1,������Ϊһ���µ�����
		
		while(a.contains("(")||a.contains("^"))
		{
			String old_string="";
			old_string=old_string.concat(a);
			StringBuffer buffer1=new StringBuffer();
			Pattern regex_test1=Pattern.compile("(\\([^()]+\\)|[0-9.]+|[a-zA-Z]+)\\^([.0-9]+)"); 
			Matcher regex_test1_matcher=regex_test1.matcher(a);
			while(regex_test1_matcher.find())
			{
				String to_insert=regex_test1_matcher.group(1);
				for(int i=1;i<Integer.parseInt(regex_test1_matcher.group(2));i++)
				{
					to_insert+="*"+regex_test1_matcher.group(1);				
				}
				regex_test1_matcher.appendReplacement(buffer1, to_insert);
			}
			regex_test1_matcher.appendTail(buffer1);
			a=buffer1.toString();//����Ϊ�ݵĴ���	
		    
		//	System.out.println(a);	
		    
		    
	    	a=a.replaceAll("([%0-9.a-zA-Z]+)\\*\\(([%0-9.a-zA-Z*]+)\\+([^()]*)\\)","($1*$2+$1*($3))");
			a=a.replaceAll("\\(([%0-9.a-zA-Z*]+)\\+([^()]*)\\)\\*([%0-9.a-zA-Z]+)","($1*$3+($2)*$3)");

		    a=a.replaceAll("\\(([%0-9.a-zA-Z*]+?)\\+([^()]*)\\)\\*\\(([%0-9.a-zA-Z*]+?)\\+([^()]*)\\)","($1*$3+$3*($2)+$1*($4)+($2)*($4))");
			//ȥ�˺�,������
		    		    
		    a=a.replaceAll("\\(([.0-9a-zA-Z%*]*)\\)","$1");
		    a=a.replaceAll("\\(\\(([^()]+)\\)\\+", "($1+");
		    a=a.replaceAll("\\+\\(([^()]+)\\)\\)", "+$1)");
		    a=a.replaceAll("\\+\\(([^()]+)\\)\\+", "+$1+");
	    	a=a.replaceAll("^\\(([^()]+)\\)\\+","$1+");
	    	a=a.replaceAll("\\+\\(([^()]+)\\)$","+$1");
	    	// ȥ����
	    	a=a.replaceAll("^\\(([^()]+)\\)$","$1");
	    	// ȥ��������  ��12+3432*67��
	    	
	    	if(a.equals(old_string))
	    	{
	    		System.out.println("Can not simpify !");
	    		System.out.println("Check syntax !");
	    		return "";
	    	}
		}
		
		return a;
	}
	    
	public static void string_to_array(String a)//�Ѵ�������ַ����洢�ھ�����
	{
	//	System.out.println(a);
		
		exponent_matrix =new int[MAX_NUMEBR][BIG_NUMEBR];
		coefficient_array = new double[MAX_NUMEBR];
		String string_matrix[]=a.split("\\+");		

		
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			coefficient_array[i]=0;
		}
		
		for(int i=0;i<string_matrix.length;i++ )
		{
			coefficient_array[i]=1;
			
			StringBuffer buffer1=new StringBuffer();
			Pattern regex_test1=Pattern.compile("([.0-9]+)");
			Matcher regex_test1_matcher=regex_test1.matcher(string_matrix[i]);
			while(regex_test1_matcher.find())
			{
				coefficient_array[i]*=Double.parseDouble(regex_test1_matcher.group(0));
				String to_insert="";
				regex_test1_matcher.appendReplacement(buffer1, to_insert);
			}
			regex_test1_matcher.appendTail(buffer1);
			string_matrix[i]=buffer1.toString();			
			
			int coefficient_check_number=0;
			for(int j=0;j<string_matrix[i].length();j++)
			{//��%���ֵĴ���
				if(string_matrix[i].charAt(j)=='%')
				{
					coefficient_check_number++;
				}
			}
			if(coefficient_check_number%2==1)
			{
				coefficient_array[i]*=-1;
			}//�������	
			string_matrix[i]=string_matrix[i].replaceAll("%", "");//�޳�%	
			string_matrix[i]=string_matrix[i].replaceAll("\\*", "");//�޳�*	
			//����Ϊ��2.34��%֮�����ݵĴ���������֮��ÿ���ַ�����������you֮���
			
			
			//System.out.println(string_matrix[i]);
			//System.out.println(string_matrix[i].length());
			//System.out.println("=================================");
			for(int j=0;j<string_matrix[i].length();j++)
			{
				exponent_matrix[i][(int)(string_matrix[i].charAt(j)-'a')]++;
			}
		}
		//���ݴ洢�������� 
		
		//���½��кϲ�ͬ����ͻ���
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			if(zero(coefficient_array[i]))
			{
				continue;
			}
			for(int j=i+1;j<MAX_NUMEBR;j++)
			{
				if(zero(coefficient_array[j]))
				{
					continue;
				}//ϵ��Ϊ��ֱ������
				
				int check=1;
				for(int k=0;k<BIG_NUMEBR;k++)
				{
					if(exponent_matrix[i][k]!=exponent_matrix[j][k])
					{
						check=0;
						break;
					}
				}
				if(check==1)
				{
					coefficient_array[i]+=coefficient_array[j];
					coefficient_array[j]=0;
				}
			}
		}
	}
	
	public static String matrix_to_string(int a[][],double b[])//�Ѿ���洢ת��Ϊ�ַ���
	{
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			if(zero(b[i]))
			{
				continue;
			}
			for(int j=i+1;j<MAX_NUMEBR;j++)
			{
				if(zero(b[j]))
				{
					continue;
				}//ϵ��Ϊ��ֱ������
				
				int check=1;
				for(int k=0;k<BIG_NUMEBR;k++)
				{
					if(a[i][k]!=a[j][k])
					{
						check=0;
						break;
					}
				}
				if(check==1)
				{
					b[i]+=b[j];
					b[j]=0;
				}
			}
		}
		//�ϲ�ͬ����
		
		String to_return ="";
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			if(!zero(b[i]))	
			{	
				if(b[i]>0)
				{
					to_return=to_return.concat("+"+Double.toString(b[i]));
					for(int j=0;j<BIG_NUMEBR;j++)
					{
						for(int k=0;k<a[i][j];k++)
						{
							char temp=(char) (j+'a');
							to_return=to_return.concat("*"+temp);
						}
					}
				}
				else
				{
					to_return=to_return.concat(Double.toString(b[i]));
					for(int j=0;j<BIG_NUMEBR;j++)
					{
						for(int k=0;k<a[i][j];k++)
						{
							char temp=(char) (j+'a');
							to_return=to_return.concat("*"+temp);
						}
					}					
				}
			}
		}
		
		to_return=to_return.replaceAll("^\\+(.*)$", "$1");	//ȥ�����ʽ��ͷ�ļӺ�	
		
		
		to_return=to_return.replaceAll("(\\.[0-9]*[1-9]+)0{5,}[1-9]{1,}","$1");//���java�����澫�ȵ�����
		//ĳ��С�������ֺܶ��0��ĩβһ�����㣬ֱ����ȥ��β�ķ��㡣
		if(to_return.isEmpty())
		{
			to_return="0";
		}
		return to_return;
	}
	
	public static String simplify(String command)//�����н��л�������
	{
		int matrix_calculate[][]=new int[MAX_NUMEBR][BIG_NUMEBR];
		double coefficient_calculate[]=new double[MAX_NUMEBR];
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			coefficient_calculate[i]=coefficient_array[i];
		}
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			for(int j=0;j<BIG_NUMEBR;j++)
			{
				matrix_calculate[i][j]=exponent_matrix[i][j];
			}
		}
		
		command=command.replaceAll("^!simplify[\\s]*","");
		//while (command.contains("  "))
		//{
			command=command.replaceAll("  "," ");
//		}
		String command_list[]=command.split(" ");
		for(int i=0;i<command_list.length;i++)
		{
			int index=command_list[i].charAt(0)-'a';
			double value=Double.parseDouble(command_list[i].substring(2));
			for(int j=0;j<MAX_NUMEBR;j++)
			{
				if(!zero(coefficient_calculate[j])&&matrix_calculate[j][index]!=0)
				{
					coefficient_calculate[j]*=Math.pow(value, matrix_calculate[j][index]);
				}
				matrix_calculate[j][index]=0;
			}
		}
		return matrix_to_string(matrix_calculate, coefficient_calculate);			
	}
	
	public static String deriative(String command)//�����н���������
	{
		int index=command.charAt(4)-'a';
		
		int matrix_calculate[][]=new int[MAX_NUMEBR][BIG_NUMEBR];
		double coefficient_calculate[]=new double[MAX_NUMEBR];
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			coefficient_calculate[i]=coefficient_array[i];
		}
		for(int i=0;i<MAX_NUMEBR;i++)
		{
			for(int j=0;j<BIG_NUMEBR;j++)
			{
				matrix_calculate[i][j]=exponent_matrix[i][j];
			}
		}

		for(int i=0;i<MAX_NUMEBR;i++)
		{
			coefficient_calculate[i]*=matrix_calculate[i][index];
			if(matrix_calculate[i][index]!=0)
			{
				matrix_calculate[i][index]--;
			}
		}
		
		return matrix_to_string(matrix_calculate, coefficient_calculate);
	}
		
	public static void all_else()
	{
		String a="a+b+asd^3+(fas)(asd)c";//֮ǰ����bug��ԭ����100���MAX_NUMEBR���õ�̫С��

		//a=myinput.read_string();
		//a="(1+2)2";
		//!simplify a=1.001 b=1.5 c=1.333 d=0 f=1 g=2 h=10
		//!simplify a=1.001 b=1.5 c=1.333   g=2 h=10//������������Ҫ���������ʾ 
		while(!syntax_check(a))
		{
			System.out.println("Syntax error! Input again");
			a=myinput.read_string();			
		}
		a=initialize(a);
		System.out.println(a);
		string_to_array(a);
		
		System.out.println("Simplified expression:");
		System.out.println(matrix_to_string(exponent_matrix, coefficient_array));
		System.out.println("Input command:");		
	}
	
	public static void main(String args[])
	{
		all_else();
		System.out.println(simplify("!simplify a=1."));
//		String command="";		
//		while(true)
//		{
//			command=myinput.read_string();
//			if(command.matches("^!simplify[\\s]+([a-zA-Z]=[-0-9.]+[\\s]*)+$"))
//			{
//				System.out.println("Simplify:");	
//				System.out.println(simplify(command));
//			}
//			else if(command.matches("^!d/d[a-zA-Z]$"))
//			{
//				System.out.println("Deriative:");
//				System.out.println(deriative(command));
//			}
//			else if(command.matches("^![\\s]*d\\/d[\\s]*$"))
//			{
//				System.out.println("No variable .");				
//			}
//			else if(command.contains("quit")||command.contains("exit"))
//			{
//				System.out.println("Over.");				
//				break;
//			}
//			else if(command.matches("^!simplify[\\s]*$"))
//			{
//				System.out.println(matrix_to_string(exponent_matrix, coefficient_array));
//			}
//			else
//			{
//		    	System.out.println("Input error !");			
//			}			
//		}
	}

}
