package wuliao.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class PrintSeveralLine implements PrintSeveralLine_Component{
	private JFrame frame; // 主窗口
	private JPanel contentPanel; // 主面板
	
	private JPanel functionPanel; //功能面板
	private JPanel inputPanel; //输入面板
	
	private JScrollPane textPanel; // 打印文本显示区
	private JTextArea textArea; // 打印文本显示区
	
	GridBagConstraints con;
	
	private int line = 0;
	private List<JTextField> texts;

	// 获取js脚本引擎(?)
	ScriptEngineManager manager = new ScriptEngineManager();
	ScriptEngine engine = manager.getEngineByName("js");
	
	private PrintSeveralLine(){
		initConfig();
		initFrame();
		initFucntionPanel();
		initActionListener();
		
		frame.pack();

		// 设置窗口居中显示
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setLocation((width - frame.getWidth()) / 2, (height - frame.getHeight()) / 2); 
		
		frame.setVisible(true);
	}

	/** 在面板中添加组件的方法(简化版) */
	private void add(JComponent panel, JComponent component, int x, int y){
		add(panel, component, x, y, 1, 1);
	}
	
	/**
	 * 在面板中添加组件的方法
	 * @param panel 面板
	 * @param component 组件
	 * @param x 列
	 * @param y 行
	 * @param w 宽(占用列数)
	 * @param h 高(占用行数)
	 */
 	private void add(JComponent panel, JComponent component, int x, int y, int w, int h){
		con.anchor = GridBagConstraints.WEST;
		
		con.gridx = x;
		con.gridy = y;
		con.gridwidth = w;
		con.gridheight = h;
		
		panel.add(component, con);
	}
	
	private void initActionListener() {
		addString.addActionListener((ActionEvent e) -> 
		{
			JTextField text = new JTextField(42);
			texts.add(text);
			text.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			add(inputPanel, texts.get(texts.size()-1), 0, line++);
			frame.pack();
		});
		
		addFormula.addActionListener((ActionEvent e) ->
		{
			JTextField formula = new JTextField(50);
			formula.setBackground(new Color(25, 25, 25));
			formula.setFont(new Font("consolas", Font.PLAIN, 14));
			formula.setForeground(new Color(190, 238, 28));
			texts.add(null);
			texts.add(formula);
			add(inputPanel, formula, 0, line++);
			frame.pack();
		});
		
		setOut.addActionListener((ActionEvent e) ->
		{
			textArea.setText(""); // 清空文本区内容
			
			String temp = countInput.getText();
			Integer countNum = Integer.valueOf(temp); // 获取循环次数
			
			temp = startNumInput.getText();
			Integer initNum = Integer.valueOf(("".equals(temp))?"0":temp); //获取起始数(默认为0)

			for(int i=0; i<countNum; i++)
			{
				StringBuilder str = new StringBuilder();
				for(int j=0; j<texts.size(); j++)
				{
					JTextField tField = texts.get(j); // 获取输入框
					if(tField == null){ // 输入框为null(公式标记)
						try {
							String mula = texts.get(++j).getText(); // 获取公式字符串
							
							engine.put("i", initNum);
							// 计算公式
							String result = engine.eval(mula).toString();
//							BigDecimal res = new BigDecimal(re);
//							String result = res.toString();
	                        str.append(result.endsWith(".0")?result.substring(0, result.indexOf('.')):result); // 计算结果放入字符串
	                        continue;
                        } catch (Exception e1){
                        	e1.printStackTrace();
                        }
					}else{ // 输入框为文本
						str.append(tField.getText()); // 将文本放入字符串
					}
				}
				// 添加字符串并换行
				textArea.append(str.toString());
				textArea.append("\n");
				initNum++;
			}
		});
    }

	/** 初始化功能区 */
	private void initFucntionPanel() {
		functionPanel.add(count);
		functionPanel.add(countInput);
		functionPanel.add(startNum);
		functionPanel.add(startNumInput);
		functionPanel.add(addString);
		functionPanel.add(addFormula);
		functionPanel.add(setOut);
    }
	
	
	/** 初始化整个窗口 */
	private void initFrame() {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
	        e.printStackTrace();
        }
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setResizable(false);

		contentPanel.setLayout(new GridBagLayout()); // 设置布局管理器
		inputPanel.setLayout(new GridBagLayout());
		
		textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 文本区字体
		
		textPanel.setViewportView(textArea); // 文本区加入滚动面板中
		
		startNumInput.setText("1");
		
		add(contentPanel, functionPanel, 0, 0);
		add(contentPanel, inputPanel, 0, 1);
		add(contentPanel, textPanel, 0, 2);
    }

	/** 初始化所有对象 */
	private void initConfig() {
		frame = new JFrame("文本批量打印机"); // 主窗口
		
		contentPanel = (JPanel) frame.getContentPane(); // 主面板
		
		functionPanel = new JPanel(); // 功能面板
		inputPanel = new JPanel(); // 输入面板

		textPanel = new JScrollPane(); // 滚动面板
		textArea = new JTextArea(15, 42); // 文本区
		
		con = new GridBagConstraints(); // 布局管理器
		
		texts = new ArrayList<JTextField>(); // 
    }
	

	public static void main(String[] args) {
	    new PrintSeveralLine();
    }
}
