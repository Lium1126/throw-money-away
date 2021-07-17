package jp.ac.ait.k19061;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

public class ThrowMoneyAway {

    // 入力用Scanner
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // "data.dat"のファイル名で保存されたデータがある場合、それを読み込んで以下の初期化は飛ばす
        ChargeData data = null;

        if (Files.exists(Path.of("data.dat"))) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Path.of("data.dat")))) {
                // ObjectをChargeDataにキャストして読み込み
                data = (ChargeData)ois.readObject();

                System.out.println("前回までのチャレンジ結果");
                System.out.println("累積課金額:" + data.getChargedTotal());
                System.out.println("これまでの挑戦回数:" + data.getChallengeCount());
                System.out.println("これまでのSSR獲得数:" + data.getHitCount());
                System.out.println();

            } catch (ClassNotFoundException | InvalidClassException e) {
                System.out.println("データファイルが壊れている可能性があります。");
                data = init();
            } catch (IOException e) {
                System.out.println("データ取得時に例外が発生しました。");
                data = init();
            }
        } else {
            // "data.dat"がない場合、もしくは読み込み失敗した場合、以下の初期化は行う
            data = init();
        }

        // ガチャデータの表示
        System.out.println("課金単価: " + data.getUnitPrice() + "円");
        System.out.println("SSRの確率: " + data.getChanceOfWinning() + "%");

        // SSRが出るか、諦めるまで無限ループ
        while (doChallenge(data)) {

            // "data.dat"に課金のデータを保存する
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Path.of("data.dat")))) {
                //Object書き出し
                oos.writeObject(data);
                //強制保存
                oos.flush();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

        }

        // ガチャへの無謀な挑戦の結果表示
        System.out.println("課金回数: " + data.getChallengeCount() + "回");
        System.out.println("課金額合計: " + data.getChargedTotal() + "円");
        System.out.println("SSR出現総数: " + data.getHitCount() + "回");

        // 当選していようがいまいが、"data.dat"に課金のデータを保存する
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Path.of("data.dat")))) {
            //Object書き出し
            oos.writeObject(data);
            //強制保存
            oos.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * ガチャ引くぞ！
     * @param data 課金データを渡す
     * @return trueで継続、falseで終了
     */
    private static boolean doChallenge(ChargeData data) {
        System.out.println("操作を選んで下さい。\n(1:課金してガチャを引く, それ以外:諦める)");

        int command = 0;
        try {
            command = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            // 例外発生時は諦める選択とする
            command = 0;
        }

        if (command != 1) {
            System.out.println("「諦める」を選択しました。お疲れさまでした。");
            return false;
        }

        System.out.println("「課金してガチャを引く」を選択しました。");

        // 演出のための待ち時間
        for (int i = 0; i < 5; i++) {
            System.out.print("* ");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();

        // ガチャを引く
        if (data.challenge()) {
            System.out.println("ガチャ結果: SSR の景品が出ました。");
            System.out.println("おめでとうございます！お疲れさまでした。");
            return false;
        }

        // ガチャの結果(SSRじゃなければ当たりじゃない)
        String[] rare = {"SR++", "SR+", "SR", "R++", "R+", "R", "N++", "N+", "N"};
        System.out.println("ガチャ結果: " + rare[new Random().nextInt(rare.length)]);

        // trueを返すことで挑戦継続
        return true;
    }

    /**
     * ガチャの基本データを初期化する
     * @return 作成したガチャの基本データ
     */
    private static ChargeData init() {
        // ※↓初期化
        int price = 300;
        double ssr = 1.0;
        System.out.println("課金データが見つかりませんでした。初期設定を行います。");

        try {
            System.out.println("ガチャの基本データ:課金単価を整数値で入力してください。");
            price = Integer.parseInt(sc.nextLine());
            System.out.println("ガチャの基本データ:SSRの出現確率を実数値で入力してください。");
            ssr = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("入力エラー");
            // 例外発生時は初期値とする(めんどうなのでここは何もしない)
        }
        // ガチャの基本データを設定して初期化
        return new ChargeData(price, ssr);
        // ※↑ここまで初期化
    }
}
