# gpxForJava
　ルートラボのGPXファイルを加工して、javaで扱えるようにします。GPXファイル→Point3D→Segmentの順にデータをオブジェクトに加工していきます。その後、Humanprofileクラスにて、あなたの体重やパワープロファイル、走行時の前面投影面積などを設定。
　最終的にSimulateTTクラスのコンストラクタへ<Segment>ListおよびHumanprofileオブジェクトを渡し、仮想環境でタイムトライアルをシミュレートします。

クラス Point3D {//読み込んだGPXファイルの3次元座標を格納するクラスです。このオブジェクトを大量に格納したArrayListを扱ってコースを作っていきます。
  
  メソッド  createPointList(String adress){//指定したアドレスのGPXファイルから座標情報を取得して、オブジェクトを生成するメソッドです。それらを格納したArrayListを戻します。


クラス Coords  {//2点の座標の水平距離を求めるクラスです。Segmentオブジェクトを作成する際に自動で使用されます。


クラス Segment {//2点間の距離・落差・斜度などを格納するクラスです。このオブジェクトをList化したものがコースになります。このSegmentに含まれるPoint3Dオブジェクトもすべて格納します。

  メソッド　createCource(ArrayList<Point3D> CP){// Point3DのArrayListを,SegmentのArrayListに加工するメソッドです。

  メソッド  concatFromProfile(ArrayList<Segment> CPL) {//createCourceメソッドにて作成したArraylistを渡して、隣り合うsegmentが同じprofile（傾斜状況）の場合、それら結合します。たとえばsegment[1]が平坦ならば、次に上り坂か下り坂が現れるまでひとまとめのsegmentとしてインスタンス同士を結合します。

  メソッド　concatFromDist(ArrayList<Segment> SL, int distance) {//createCourceメソッドにて作成したArraylistを渡して、distance(m)未満のsegmentがある場合は、distanceより大きくなるまで隣の区間とに結合しつづけます。

  メソッド  concatDefault(ArrayList<Segment> SL)//こちらで設定した規則に基づいてsegmentを扱いやすい長さに結合します。
  
クラス
　　　　　　　　　　　　　　　　　　　　　　　　　　
