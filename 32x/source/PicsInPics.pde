import java.io.*;

PGraphics canvas;
PVector piecesize;

String[] pref;
Original[] original;
Piece[] piece;

String msg;

void setup()
{
  size(1500,1000);
  textAlign(LEFT,TOP);
  textSize(50);
  fill(255);
  thread("t_main");
}

void draw()
{
  background(180);
  if(canvas!=null);
    image(canvas,0,0);
  text(msg,0,0);
}

void t_main()
{
  msg = "loading pref.txt";
  pref = loadStrings("pref.txt");
  canvas = createGraphics(int(pref[0]),int(pref[1]));
  piecesize = new PVector(int(pref[2]),int(pref[3]));
  
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
      msg = "loading/calculating original images "+floor((i+1.0)/ofiles.length*100)+"%";
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
      msg = "loading/calculating piece images "+floor((i+1.0)/pfiles.length*100)+"%";
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
          color pc = p.avgcolor;
          color oc = o.avgcolor[x+y*o.xcount];
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
    msg = "processing Pics-In-Pics "+floor((i+1.0)/original.length*100)+"%";
  }
  
  msg = "done";
}
