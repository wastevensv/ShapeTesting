/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapetesting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import shapetesting.graphs.Node;

public class ShapeTesting {

    public JFrame frame = new JFrame();
    public ShapeTesting() {
        frame.setTitle("Shape Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        initComponents(frame);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        //create frame and components on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShapeTesting();
            }
        });
    }

    private void initComponents(JFrame frame) {
        frame.add(new ShapePanel());
    }
}

//custom panel
class ShapePanel extends JPanel {
    private Dimension dim = new Dimension(900, 600);
    private final ArrayList<Node> nodes;
    private final Stroke stroke;
    private int dragging = -1;
    
    public ShapePanel() {
        stroke = new BasicStroke((float) 2.5);
        nodes = new ArrayList<>();
        nodes.add(new Node(50,50,100,"A",Color.blue));
        nodes.add(new Node(150,50,100,"B",Color.red));
        nodes.add(new Node(250,50,100,"C",Color.green));
        nodes.add(new Node(50,150,100,"D",Color.orange));
        nodes.add(new Node(150,150,100,"E",Color.black));
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                //System.out.println("Dragged Node "+dragging);
                if(dragging != -1) {
                    nodes.get(dragging).setLocation(me.getX(), me.getY());
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            private int start = -1;
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                for (Node node : nodes) {
                    Shape s = node.getCircle();
                    if (s.contains(me.getPoint())) {//check if mouse is clicked within shape
                        if(me.getButton() == MouseEvent.BUTTON1) {
                            if(me.getClickCount() == 1) {
                                if(start == -1) {
                                    start = nodes.indexOf(node);
                                    //Print Node Name
                                    System.out.println("Clicked Node "+node.getName());
                                } else {
                                    Node n=nodes.get(start);
                                    n.addConnection(node);
                                    System.out.println("Clicked Node "+n.getName()+" to "+node.getName());
                                    start = -1;
                                    repaint();
                                }
                            } else if(me.getClickCount() == 2) {
                                start = -1;
                                String str = (String)JOptionPane.showInputDialog(
                                    ShapePanel.this,
                                    "Node Name:\n",
                                    "Node Name",
                                    JOptionPane.PLAIN_MESSAGE);
                                if(str == null) return;
                                node.setName(str);
                                repaint();
                            }
                        } else {
                            start = -1;
                            System.out.println("Declicked Node");
                            nodes.remove(node);
                            repaint();
                        }
                        return;
                    }
                }
                System.out.println("No node clicked");
                start = -1;
                if(me.getClickCount() == 2) {
                    nodes.add(new Node(me.getX(),me.getY(),100,"N"));
                }
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent me) {
                super.mousePressed(me);
                if(dragging == -1) {  
                    for (Node node : nodes) {
                        Shape s = node.getCircle();
                        if (s.contains(me.getPoint())) {//check if mouse is clicked within shape
                            dragging = nodes.indexOf(node);
                            //Print Node Name
                            System.out.println("Pressed Node "+node.getName());
                            break;
                        }
                    }
                } else {
                    nodes.get(dragging).setLocation(me.getX(), me.getY());
                    System.out.println("Presed Node "+nodes.get(dragging).getName());
                }
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
                super.mouseReleased(me);
                if(dragging == -1) return;
                System.out.println("Released Node "+nodes.get(dragging).getName());
                dragging = -1;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        for (Node node : nodes) {
            node.drawCircle(g2d);
            node.drawConnectors(g2d);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }
}