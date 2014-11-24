/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapetesting.graphs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 *
 * @author William
 */
public class Node {
    private Ellipse2D circle;
    private double x;
    private double y;
    private double s;
    private String name;
    private Color color;
    private ArrayList<Node> connections = new ArrayList<>();
    private ArrayList<Node> connectors = new ArrayList<>();

    public Node(int cx, int cy, int s) {
        this(cx,cy,s,"No Name");
    }
    
    public Node(double cx, double cy, double s, String name) {
        this(cx,cy,s,name,Color.black);
    }
    
    public Node(double cx, double cy, double s, String name, Color color) {
        this.x = cx;
        this.y = cy;
        this.s = s;
        circle = new Ellipse2D.Double(cx-(s/2), cy-(s/2), s, s);
        this.name = name;
        this.color = color;
    }
    
    public void addConnection( Node n ) {
        if(n == this) return;
        if(connections.contains(n)) return;
        connections.add(n);
        n.addConnector(this);
    }    
    public void addConnector( Node n ) {
        if(n == this) return;
        if(connections.contains(n)) return;
        connectors.add(n);
    }     
    
    public void setLocation( double cx, double cy ) {
        this.x = cx;
        this.y = cy;
        circle = new Ellipse2D.Double(cx-(s/2), cy-(s/2), s, s);
    } 
    
    public void setName( String name ) {
        this.name = name;
    }
    
    public void drawConnectors(Graphics2D g2d ) {
        Color defcolor = g2d.getColor();
        g2d.setColor(color);
        for(Node n : connections) {
            Line2D connector = new Line2D.Double(x,y,n.getX(),n.getY());
            g2d.draw(connector);
        }
        g2d.setColor(defcolor);
    }
    
    public void drawCircle(Graphics2D g2d) {
        Color defcolor = g2d.getColor();
        g2d.setColor(color);
        g2d.draw(circle);
        g2d.drawString(name, (float)x, (float)y);
        g2d.setColor(defcolor);
    }
    
    public ArrayList<Node> getConnections() {
        return connections;
    }
    
    public ArrayList<Node> getConnectors() {
        return connectors;
    }
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return name+" "+x+" "+y;
    }

    public Ellipse2D getCircle() {
        return circle;
    }
}
