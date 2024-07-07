鹰鸽博弈的结果具有很大的随机性，且随着不同的参数波动很多。
其中关于付出与得到对人们心态的影响设定对结果影响显著，即以下代码：

```
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

因为我认为人类随着经济变好合作意愿提到程度大于经济受伤时的程度，可以看到在1000次交锋之后，社会上所有人都变成了鸽子。