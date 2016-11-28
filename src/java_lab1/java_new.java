/* 1: ֧�ּ��źͿո�@
 * 2��֧�ֶ����ĸ֮�䣬��ĸ����֮��ʡ�Գ˺�
 * 3��֧�ָ�����
 * 4��֧�������﷨���
 * 5��֧��ͬʱ��ֵ�������
 * 6��֧�ָ��������ݣ�֧�����������Ƕ�׺���ݣ�֧�ָߴΣ�ʮλ�����ϣ�������
 * 7: ֧�ֱ��ʽ�����﷨���
 * */


package java_lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;

class matrix_and_array
{//ʵ��1
	final static double EPS=1e-6;//�ж�ĳdouble�Ƿ�Ϊ0��
	final static int MAX_NUMEBR = 1000;//���֧������
	final static int BIG_NUMEBR = 30;//������
	private static  int[][]  exponent_matrix;
	private static double[] coefficient_array; 	

	public static boolean zero(double a)
	{
		return (a<EPS)&&(a>-EPS);
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
		while (command.contains("  "))
		{
			command=command.replaceAll("  ", " ");
		}
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

	public static String big(String command)
	{
		if(command.matches("^!simplify[\\s]+([a-zA-Z]=[-0-9.]+[\\s]*)+$"))
		{
			return (simplify(command));
		}
		else if(command.matches("^!d/d[a-zA-Z]$"))
		{
			return (deriative(command));
		}
		else if(command.matches("^![\\s]*d\\/d[\\s]*$"))
		{
		//	System.out.println("No variable .");				
		}
		else if(command.contains("quit")||command.contains("exit"))
		{
	//		System.out.println("Over.");				
	//		break;
		}
		else if(command.matches("^!simplify[\\s]*$"))
		{
			return (matrix_to_string(exponent_matrix, coefficient_array));
		}
		else
		{
//	    	System.out.println("Input error !");			
		}			
		return "hehe";
	}
}

class main_expression
{//ʵ��2
	final static double EPS=1e-6;//�ж�ĳdouble�Ƿ�Ϊ0��	
	final static int CHECK_NUMEBR = 30;//������	
	private static String a;
	
	public static boolean zero(double a)
	{
		return (a<EPS)&&(a>-EPS);
	}
	
	public static int initialize()//���ַ�������Ԥ����
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
	 //   System.out.println(a);		
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
	 //   		System.out.println("Can not simpify !");
	  //  		System.out.println("Check syntax !");
	    		return -1;
	    	}
		}		
		return 1;
	}
	
	public static boolean syntax_check()//���ʽ�����﷨���
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
	
	public static void set_expression(String t)
	{
		a=t;
	}

	public static String inner_string()
	{
		return a;
	}
}



class command_string
{//ʵ��3
	private static String command;	

	public static void set_expression(String a)
	{
		command=a;		
	}	
	
	public static String get_expression()
	{
		return command;		
	}	
}

class in_out
{//�߽�1
	public static String read_string()
	{
		BufferedReader br=new BufferedReader( new InputStreamReader(System.in),1);
		String string="";
		try {
			string = br.readLine();
		} catch (IOException ex) 
		{
			System.out.println(ex);
		}
		return string;
	}
	
	public static int read_int()
	{
		return Integer.parseInt(read_string());
	}

	public static void print_num(int to_print)
	{
		if(to_print==1)
		{
			System.out.print("Expression Wrong!");			
		}
		if(to_print==2)
		{	
			System.out.print("Input expression:\n");	
		}
		if(to_print==3)
		{	
			System.out.print("Input command:\n");	
		}
	}
}


class control
{//����2
	public void bingo()
	{
//		main_expression a=new main_expression();
//		in_out string_in=new in_out();
//		matrix_and_array matrix_here=new matrix_and_array();
//		command_string command_here=new command_string();
//		
		
		

		in_out.print_num(2);
		while(true)
		{			
			main_expression.set_expression(in_out.read_string());
			if(main_expression.initialize()==1&&main_expression.syntax_check())
			{
				matrix_and_array.string_to_array(main_expression.inner_string());
				in_out.print_num(3);			
				command_string.set_expression(in_out.read_string());
				System.out.println(matrix_and_array.big(command_string.get_expression()));					
			}
			else 
			{
				in_out.print_num(1);
			}			
			in_out.print_num(2);
		}
	}
}

public class java_new 
{
	public static void main(String args[])
	{		
		control this_control= new control();
		this_control.bingo();
	}
}
