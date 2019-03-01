public class Original
{
  public String imagepath;
  public color[] avgcolor;
  public int xcount;
  public int ycount;
  
  public Original(String imagepath)
  {
    this.imagepath = imagepath;
    PImage image = loadImage(this.imagepath);
    image.loadPixels();
    xcount = ceil(image.width / piecesize.x);
    ycount = ceil(image.height / piecesize.y);
    avgcolor = new color[xcount*ycount];
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
            color c = image.get(xpos+x2,ypos+y2);
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
