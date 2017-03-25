# gpxForJava
ルートラボのGPXファイルを加工して、javaで扱えるようにします。


class Point3D {//読み込んだGPXファイルの3次元座標を格納するクラスです。このオブジェクトを大量に格納したArrayListを扱ってコースを作っていきます。
メソッド  createPointList(String adress){//指定したアドレスのGPXファイルから座標情報を取得して、オブジェクトを生成するメソッドです。それらを格納したArrayListを戻します。



class Coords  {//2点の座標の水平距離を求めるクラスです。Segmentオブジェクトを作成する際に自動で使用されます。



class Segment {//2点間の距離・落差・斜度などを格納するクラスです。このオブジェクトをList化したものがコースになります。
メソッド　createCource(ArrayList<Point3D> CP){// Point3DのArrayListを,Segmentオブジェクトに加工するメソッドです。

メソッド  concatFromProfile(ArrayList<Segment> CPL) {//隣り合うsegmentが同じprofileの場合、それら結合します。たとえば区間が平坦ならば、次に上り坂か下り坂が現れるまでひとまとめのsegmentとしてインスタンス同士を結合します。

メソッド　concatFromDist(ArrayList<Segment> SL, int distance) {//distance(m)未満のsegmentがある場合は、distanceより大きくなるまで隣の区間とに結合しつづけます。

メソッドconcatDefault(ArrayList<Segment> SL)//こちらで設定した規則に基づいてsegmentを結合します。　　
　　　　　　　　　　　　　　　　　　　　　　　　　　
