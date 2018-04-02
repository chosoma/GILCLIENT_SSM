package mytools;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CreateButton extends JButton{

	private String imageURL = null;
	private ImageIcon icon , iconRover;

	public CreateButton(String imageURL){
		this.imageURL = imageURL;
		initialize();
	}

	public CreateButton(String imageURL,String text){
		super(text);
		this.imageURL = imageURL;
		initialize();
	}

	public CreateButton(String imageURL,String text,String tooltip){
		super(text);
		this.setToolTipText(tooltip);
		this.imageURL = imageURL;
		initialize();
	}

	private void initialize() {
		icon = new ImageIcon("images/"+imageURL+".png");
		ImageIcon iconPressed = new ImageIcon("images/"+imageURL+"_press.png");
		iconRover = new ImageIcon("images/"+imageURL+"_rover.png");
		this.setIcon(icon);
		this.setPressedIcon(iconPressed);
		this.setRolloverIcon(iconRover);
		this.setBorder(null);
		//取消绘制按钮内容区域
		this.setContentAreaFilled(false);
		//设置按钮按下后无虚线框
		this.setFocusPainted(false);

		this.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e) {
				CreateButton.this.setIcon(iconRover);
			}
			@Override
			public void focusLost(FocusEvent e) {
				CreateButton.this.setIcon(icon);
			}
		});

		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==10){
					CreateButton.this.doClick();
				}else
					return;
			}
		});

	}

	public void changeButton(String imageURL,String text,String tooltip){
		CreateButton.this.setText(text);
		CreateButton.this.setToolTipText(tooltip);
		this.imageURL = imageURL;
		this.initialize();
	}

}
