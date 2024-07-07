鹰鸽博弈的结果具有很大的随机性，且随着不同的参数波动很多。
其中关于付出与得到对人们心态的影响设定对结果影响显著，即以下代码：

```sh
def pigeon_score_changed_with_money_changed(human, paid, got):
    if paid > got:
        if human.is_pigeon:
            human.pigeon_score = max(human.pigeon_score - PIGEON_SCORE_PER_ROUND, 0)
        else:
            # 如果不是鸽子且付出大于得到，会进一步恶化它合作意愿
            human.pigeon_score = max(human.pigeon_score - PIGEON_SCORE_PER_ROUND / 100., 0)
    elif got > paid:
        if human.is_pigeon:
            human.pigeon_score = min(human.pigeon_score + PIGEON_SCORE_PER_ROUND, 1)
        else:
            # 如果不是鸽子且得到大于付出，随着经济状况变好，合作意愿增加1/10
            human.pigeon_score = min(human.pigeon_score + PIGEON_SCORE_PER_ROUND / 10., 1)
```
下面是一次运行的结果可供参考：
```shell
program costed seconds: 50.0095911026001
init status: total 10000,50.2% are pigeon; money: pigeons 25100, eagles 24900
last status: total 3766,100.0% are pigeon; money: pigeons 84257406.29801074, eagles 0
death: 10000.0 humans,62% dead, 39% are pigeon
```

因为我认为人类随着经济变好合作意愿提到程度大于经济受伤时的程度，可以看到在1000次交易之后，社会上所有人都变成了鸽子。
那么如果我们根本交易不到这么多次呢？其实在我们生命中，能真的有影响的交易应该确实没有太多次。
如果我们认为付出与得到的影响应该更加显著，
```sh
def pigeon_score_changed_with_money_changed(human, paid, got):
    if paid > got:
        if human.is_pigeon:
            human.pigeon_score = max(human.pigeon_score - PIGEON_SCORE_PER_ROUND, 0)
        else:
            # 如果不是鸽子且付出大于得到，会进一步恶化它合作意愿
            human.pigeon_score = max(human.pigeon_score - PIGEON_SCORE_PER_ROUND / 10., 0)
    elif got > paid:
        if human.is_pigeon:
            human.pigeon_score = min(human.pigeon_score + PIGEON_SCORE_PER_ROUND, 1)
        else:
            # 如果不是鸽子且得到大于付出，随着经济状况变好，合作意愿增加1/10
            human.pigeon_score = min(human.pigeon_score + PIGEON_SCORE_PER_ROUND / 10., 1)
```
10次交锋的参考结果如下：
```sh
program costed seconds: 0.9808392524719238
init status: total 10000,48.699999999999996% are pigeon; money: pigeons 24330, eagles 25670
last status: total 3523,21.6% are pigeon; money: pigeons 33491.02487454421, eagles 57608.18046785566
death: 10000.0 humans,65% dead, 38% are pigeon
```
可以看到此时鹰派其实是更为受益的。事实上，如果付出与得到的影响更加显著时，交锋次数提高到20次以上，基本上社会最后就只会剩下一个鹰派。

以上结果怎么解读合适呢？我想社会的复杂程度不是这个小程序能模拟的，所以结果究竟如何并没有意义，我们也不可能知道当下这个社会的最佳“做人策略”。但是
这个结果警告我们一件事，那就是没有最优的“做人策略”，单纯从利益上来说，做鹰还是鸽或者其他的什么人取决于每个人自己所处的复杂的环境。