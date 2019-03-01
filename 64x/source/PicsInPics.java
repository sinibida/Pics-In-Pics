import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PicsInPics extends PApplet {



PGraphics canvas;
PVector piecesize;

String[] pref;
Original[] original;
Piece[] piece;

String msg;

public void setup()
{
  
  textAlign(LEFT,TOP);
  textSize(50);
  fill(255);
  thread("t_main");
}

public void draw()
{
  background(180);
  if(canvas!=null);
    image(canvas,0,0);
  text(msg,0,0);
}

public void t_main()
{
  msg = "loading pref.txt";
  pref = loadStrings("pref.txt");
  canvas = createGraphics(PApplet.parseInt(pref[0]),PApplet.parseInt(pref[1]));
  piecesize = new PVector(PApplet.parseInt(pref[2]),PApplet.parseInt(pref[3]));
  
  msg = "loading/calculating original images 0%";
  {
    String[] ofiles = (new File(dataPath("original"))).list();
    original = new Original[ofiles.length];
    for(int i = 0;i < ofiles.length;i++)
    {
      original[i] = new Original("original\\" + ofiles[i]);
      canvas.beginDraw();
      canvas.image(loadImage(original[i].imagepath),0,0);
      canvas.endDraw();
      msg = "loading/calculating original images "+floor((i+1.0f)/ofiles.length*100)+"%";
    }
  }
  msg = "loading/calculating piece images 0%";
  {
    String[] pfiles = (new File(dataPath("piece"))).list();
    piece = new Piece[pfiles.length];
    for(int i = 0;i < pfiles.length;i++)
    {
      piece[i] = new Piece("piece\\" + pfiles[i]);
      canvas.beginDraw();
      canvas.image(loadImage(piece[i].imagepath),0,0);
      canvas.endDraw();
      msg = "loading/calculating piece images "+floor((i+1.0f)/pfiles.length*100)+"%";
    }
  }
  
  msg = "processing Pics-In-Pics 0%";
  for(int i = 0;i<original.length;i++)
  {
    Original o = original[i];
    canvas.beginDraw();
    for(int x = 0;x<o.xcount;x++)
    {
      for(int y = 0;y<o.ycount;y++)
      {
        int index = 0;
        int error = Integer.MAX_VALUE;
        for(int j = 0;j<piece.length;j++)
        {
          Piece p = piece[j];
          int e = 0;
          int pc = p.avgcolor;
          int oc = o.avgcolor[x+y*o.xcount];
          e += abs(red(pc)-red(oc));
          e += abs(blue(pc)-blue(oc));
          e += abs(green(pc)-green(oc));
          if(error > e)
          {
            index = j;
            error = e;
          }
        }
        canvas.image(loadImage(piece[index].imagepath),x*piecesize.x,y*piecesize.y);
      }
    }
    canvas.save(String.format("result\\R%04d.png",i));
    canvas.endDraw();
    msg = "processing Pics-In-Pics "+floor((i+1.0f)/original.length*100)+"%";
  }
  
  msg = "done";
}
public class Original
{
  public String imagepath;
  public int[] avgcolor;
  public int xcount;
  public int ycount;
  
  public Original(String imagepath)
  {
    this.imagepath = imagepath;
    PImage image = loadImage(this.imagepath);
    image.loadPixels();
    xcount = ceil(image.width / piecesize.x);
    ycount = ceil(image.height / piecesize.y);
    avgcolor = new int[xcount*ycount];
    for(int y = 0;y<ycount;y++)
    {
      for(int x = 0;x<xcount;x++)
      {
        float sr = 0,sg = 0,sb = 0;
        int sc = 0;
        for(int x2 = 0;x2<piecesize.x;x2++)
        {
          for(int y2 = 0;y2<piecesize.y;y2++)
          {
            int xpos = floor(x*piecesize.x);
            int ypos = floor(y*piecesize.y);
            if(xpos+x2>=image.width||ypos+y2>=image.height)
              continue;
            int c = image.get(xpos+x2,ypos+y2);
            sr += red(c);
            sg += green(c);
            sb += blue(c);
            sc++;
          }
        }
        avgcolor[y*xcount+x] = color(sr/sc,sg/sc,sb/sc);
      }
    }
  }
}
public class Piece
{
  public String imagepath;
  public int avgcolor;
  
  public Piece(String imagepath)
  {
    this.imagepath = imagepath;
    PImage image = loadImage(this.imagepath);
    image.loadPixels();
    float sr = 0,sg = 0,sb = 0;
    for(int c : image.pixels)
    {
      sr += red(c);
      sg += green(c);
      sb += blue(c);
    }
    avgcolor = color(sr/image.pixels.length,sb/image.pixels.length,sg/image.pixels.length);
  }
}
  public void settings() {  size(1500,1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PicsInPics" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
