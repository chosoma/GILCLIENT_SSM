package view.debugs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import data.FormatTransfer;

import mytools.MyUtil;

public class Debugs extends JPanel {

    private JTextArea jta = null;
    private String lineFeed = "\r\n";
    private boolean isShow = false;
    private SimpleDateFormat timeFormat = MyUtil.getDateFormat();
    private JPopupMenu pop;
    private JMenuItem copy = null, cut = null, delete = null, clear = null;

    private static Debugs DBShow = new Debugs();

    private Debugs() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        init();
    }

    public static Debugs getInstance() {
        return DBShow;
    }

    private void init() {
        pop = new JPopupMenu();
        copy = new JMenuItem("复制");
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jta.copy();
            }
        });
        pop.add(copy);

        cut = new JMenuItem("剪切");
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutText();
            }
        });
        pop.add(cut);

        delete = new JMenuItem("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteText();
            }
        });
        pop.add(delete);

        clear = new JMenuItem("清空");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jta.setText("");
                jta.repaint();
            }
        });
        pop.add(clear);

        jta = new JTextArea();
        jta.setLineWrap(true);// 自动换行
        jta.setWrapStyleWord(true);// 断行不断字
        jta.setFont(MyUtil.FONT_14);
//        jta.setEditable(false);
//        jta.setEnabled(false);
        jta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                int buttonKey = arg0.getButton();
//                // 双击清屏
//                if (buttonKey == MouseEvent.BUTTON1 && arg0.getClickCount() == 2) {
//                    jta.setText(null);
//                }
                if (buttonKey == MouseEvent.BUTTON3) {
                    boolean visible = isSeleted();
                    copy.setEnabled(visible);
                    cut.setEnabled(visible);
                    delete.setEnabled(visible);
                    visible = !(jta.getText().equals(""));
                    clear.setEnabled(visible);
                    pop.show(jta, arg0.getX(), arg0.getY());
                }
            }
        });
        jta.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (isSeleted()) {
                    super.insertString(offs, str, a);
                    if (jta.getLineCount() >= 10000) {
                        jta.setText("");
                    }
                    return;
                }
                try {
                    int off = jta.getLineEndOffset(jta.getLineCount() - 1000);
                    remove(0, off);
                    super.insertString(offs - off, str, a);
                } catch (BadLocationException e) {
                    // e.printStackTrace();
                    super.insertString(offs, str, a);
                } finally {
                    repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(jta);
        this.add(scrollPane, BorderLayout.CENTER);

//        java.util.Timer timer = new java.util.Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                int length = jta.getText().length();
//                int start = (int) (Math.random() * length);
//                int end = start + (int) (Math.random() * (length - start));
//                jta.replaceRange("", start, end);
//            }
//        };
//        timer.schedule(timerTask, 0, 1000);

    }

    public synchronized void rec(byte[] data, int len, Date time, String msg) {
        if (!isShow) {
            return;
        }
        String rece = " →→收 : ";
        jta.append(timeFormat.format(time) + msg + rece
                + FormatTransfer.getBufHexStr(data, 0, len) + lineFeed);
        if (jta.getLineCount() > 1000) {
            subString();
        }
    }

    public synchronized void send(byte[] data, int len, String dtu) {
        if (!isShow) {
            return;
        }
        String send = " 发→→ : ";
        jta.append(timeFormat.format(Calendar.getInstance().getTime()) + dtu
                + send + FormatTransfer.getBufHexStr(data, 0, len) + lineFeed);
        if (jta.getLineCount() > 1000) {
            subString();
        }
    }

    private synchronized void subString() {
        try {
            if (isSeleted()) {
                if (jta.getLineCount() > 5000) {
                    jta.setText("");
                }
                return;
            }
            int off = jta.getLineEndOffset(jta.getLineCount() - 1000);
            jta.replaceRange("", 0, off);
            jta.repaint();
        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            notify();
        }
    }


    public synchronized void showMsg(String s) {
        if (!isShow) {
            return;
        }
        jta.append(timeFormat.format(Calendar.getInstance().getTime()) + s
                + lineFeed);
        if (jta.getLineCount() > 1000) {
            subString();
        }
    }

    public void setShow(boolean b) {
        isShow = b;
        if (!b) {
            jta.setText(null);
        }
    }

    // 光标是否选中
    private boolean isSeleted() {
        boolean b = false;
        if (jta.getSelectedText() != null) {
            b = true;
        }
        return b;
    }

    // 剪切
    private void cutText() {
        if (isSeleted()) {
            jta.copy();
            jta.replaceRange("", jta.getSelectionStart(), jta.getSelectionEnd());
        }
    }

    // 删除
    private void deleteText() {
        if (isSeleted()) {
            jta.replaceRange("", jta.getSelectionStart(), jta.getSelectionEnd());
        }
    }

}
