import random

from GameTheoryConstants import INIT_MONEY, PIGEON_SCORE_PER_ROUND, DOUBLE_WIN_AWARD, PER_HUMAN_COST_BASE


class Human(object):
    def __init__(self, money=INIT_MONEY, pigeon_score=0.6):
        self.pigeon_score = pigeon_score
        self.money = money

    @property
    def force_power(self):
        power = (1 - self.pigeon_score) or 0.00001
        if power < 0:
            power = 0.00001
        return float(power)

    @property
    def is_pigeon(self):
        return self.pigeon_score > 0.5

    @property
    def if_dead(self):
        return self.money < INIT_MONEY

    @staticmethod
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

    def meet(self, other_human):
        # 每个人都需要拿出这么多押金
        cost_per_human = PER_HUMAN_COST_BASE * 0.5 / 2.
        self.money -= cost_per_human
        other_human.money -= cost_per_human

        # 创造新财富
        new_money = self.created_new_money(self.pigeon_score + other_human.pigeon_score)
        round_money = cost_per_human * 2 + new_money

        # 进行财富再分配
        total_force_power = self.force_power + other_human.force_power
        i_got = self.force_power / total_force_power * round_money
        other_got = other_human.force_power / total_force_power * round_money

        # 5% 的概率异变
        i_got, other_got = self.wow_change(cost_per_human, i_got, other_got)

        self.money += i_got
        other_human.money += other_got
        self.pigeon_score_changed_with_money_changed(self, paid=cost_per_human, got=i_got)
        self.pigeon_score_changed_with_money_changed(other_human, cost_per_human, other_got)

    @staticmethod
    def wow_change(cost_per_human, i_got, other_got):
        wow = random.random() < 0.05
        double_win = i_got > cost_per_human and other_got > cost_per_human
        double_loss = i_got < cost_per_human and other_got < cost_per_human
        if double_win:
            if wow:
                other_got += i_got
                i_got = 0
            else:
                i_got *= DOUBLE_WIN_AWARD
                other_got *= DOUBLE_WIN_AWARD
        # 原本明明是双输的两只鹰，突然良心发现或者运气太好，每个人额外再补贴一次押金
        if double_loss and wow:
            other_got += cost_per_human
            i_got += cost_per_human
        return i_got, other_got

    # 1. 两只系数为0（丝毫没有合作精神）的鹰，每轮新的财富是-5；
    # 2. 两只无争无求的非鹰非鸽，每轮新的财富是0；
    # 3. 两只系数为1（超强合作意识）的鸽，每轮新的财富是5
    @staticmethod
    def created_new_money(total_pigeon_score):
        average_pigeon_score = total_pigeon_score / 2.
        return PER_HUMAN_COST_BASE * (average_pigeon_score - 0.5)
