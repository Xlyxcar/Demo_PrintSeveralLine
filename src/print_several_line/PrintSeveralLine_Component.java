package print_several_line;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public interface PrintSeveralLine_Component {

	JLabel count = new JLabel("循环次数");
	JTextField countInput = new JTextField(5);
	
	JLabel startNum = new JLabel("起始数(i)");
	JTextField startNumInput = new JTextField(5);
	
	JButton addString = new JButton("添加字符串");
	JButton addFormula = new JButton("添加公式");
	
	JButton setOut = new JButton("生成");

}
