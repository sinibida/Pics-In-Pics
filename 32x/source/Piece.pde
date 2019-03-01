public class Piece
{
  public String imagepath;
  public color avgcolor;
  
  public Piece(String imagepath)
  {
    this.imagepath = imagepath;
    PImage image = loadImage(this.imagepath);
    image.loadPixels();
    float sr = 0,sg = 0,sb = 0;
    for(color c : image.pixels)
    {
      sr += red(c);
      sg += green(c);
      sb += blue(c);
    }
    avgcolor = color(sr/image.pixels.length,sb/image.pixels.length,sg/image.pixels.length);
  }
}
