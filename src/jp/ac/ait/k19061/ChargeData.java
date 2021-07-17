package jp.ac.ait.k19061;

import java.io.Serializable;
import java.util.Random;

public class ChargeData implements Serializable {

    /**
     * ガチャ1回分の課金単価
     */
    private int unitPrice;
    /**
     * 課金額合計
     */
    private long chargedTotal;
    /**
     * SSRが出る確率(%) 小数点以下4桁まで対応
     */
    private double chanceOfWinning;
    /**
     * 挑戦回数
     */
    private long challengeCount;

    /**
     * SSRが出た回数
     */
    private int hitCount;

    /**
     * @return the unitPrice
     */
    public int getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the chargedTotal
     */
    public long getChargedTotal() {
        return chargedTotal;
    }

    /**
     * @param chargedTotal the chargedTotal to set
     */
    public void setChargedTotal(long chargedTotal) {
        this.chargedTotal = chargedTotal;
    }

    /**
     * @return the chanceOfWinning
     */
    public double getChanceOfWinning() {
        return chanceOfWinning;
    }

    /**
     * @param chanceOfWinning the chanceOfWinning to set
     */
    public void setChanceOfWinning(double chanceOfWinning) {
        this.chanceOfWinning = chanceOfWinning;
    }

    /**
     * @return the challengeCount
     */
    public long getChallengeCount() {
        return challengeCount;
    }

    /**
     * @param challengeCount the challengeCount to set
     */
    public void setChallengeCount(long challengeCount) {
        this.challengeCount = challengeCount;
    }

    /**
     * @return the hitCount
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * @param hitCount the hitCount to set
     */
    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    /**
     *
     * @param unitPrice
     * @param chargedTotal
     * @param chanceOfWinning
     * @param challengeCount
     * @param hitCount
     */
    public ChargeData(int unitPrice, long chargedTotal, double chanceOfWinning, long challengeCount, int hitCount) {
        this.unitPrice = unitPrice;
        this.chargedTotal = chargedTotal;
        this.chanceOfWinning = chanceOfWinning;
        this.challengeCount = challengeCount;
        this.hitCount = hitCount;
    }

    /**
     * 引数なしの場合は、単価300、出現確率1%で初期化
     */
    public ChargeData() {
        this(300, 0, 1, 0, 0);
    }

    /**
     * 単価と確率だけ設定して初期化
     *
     * @param unitPrice
     * @param chanceOfWinning
     */
    public ChargeData(int unitPrice, double chanceOfWinning) {
        this(unitPrice, 0, chanceOfWinning, 0, 0);
    }

    /**
     * ガチャ回す 回した回数と、課金額合計が自動的に加算されます。
     *
     * @return 設定した確率で当選可否を返します
     */
    public boolean challenge() {

        // 課金回数と課金額を加算
        this.challengeCount += 1;
        this.chargedTotal += this.unitPrice;

        long rand = new Random().nextInt(100 * 10000);  //小数点以下4桁まで対応する
        if (rand < (this.chanceOfWinning * 10000)) {
            // 当選
            // SSR当選回数をインクリメント
            this.hitCount += 1;
            return true;
        } else {
            return false;
        }
    }

}
