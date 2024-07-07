import random
import time

from GameTheoryConstants import INIT_HUMAN_NUMBER, DEFAULT_PIGEON_SCORE_RANGE, MILESTONE_TIMES
from Human import Human


class Society(object):

    def __init__(self, init_human_number=INIT_HUMAN_NUMBER, pigeon_score_range=DEFAULT_PIGEON_SCORE_RANGE):
        self.init_humans = []
        self.all_humans = []
        self.living_humans = []
        self.dead_humans = []

        for i in range(init_human_number):
            min_score, max_score = pigeon_score_range
            score = random.randint(min_score * 1000, max_score * 1000) / 1000.
            human = Human(pigeon_score=score)
            inited_human = Human(pigeon_score=score)
            self.living_humans.append(human)
            self.all_humans.append(human)
            self.init_humans.append(inited_human)

    def check_dead_humans(self, *humans):
        for human in humans:
            if human.if_dead:
                self.dead_humans.append(human)
                if len(self.living_humans) > 0:
                    self.living_humans.remove(human)

    def do_humans_meeting(self, meeting_times=None, life_meeting_times=MILESTONE_TIMES):
        t1 = time.time()
        if meeting_times is None:
            meeting_times = life_meeting_times * len(self.living_humans)
        for i in range(meeting_times):
            if len(self.living_humans) < 2:
                print('humans<2, meeting_times %s/%s' % (i + 1, meeting_times))
                break
            two_humans = random.sample(self.living_humans, 2)
            one, two = two_humans
            one.meet(two)
            self.check_dead_humans(*two_humans)
        t2 = time.time()
        print('program costed seconds: %s' % (t2 - t1))

    @staticmethod
    def get_humans_info(humans):
        pigeon_humans = []
        pigeon_money = 0
        eagle_money = 0
        for human in humans:
            if human.is_pigeon:
                pigeon_humans.append(human)
                if human.money > 0:
                    pigeon_money += human.money
            else:
                if human.money > 0:
                    eagle_money += human.money

        total_human_number = len(humans)
        pigeon_percent = round(len(pigeon_humans) / float(total_human_number), 3)
        pigeon_percent *= 100
        return ('total {0},{1}% are pigeon; money: pigeons {2}, eagles {3}'
                .format(total_human_number, pigeon_percent, pigeon_money, eagle_money))

    @staticmethod
    def get_humans_death_info(humans):
        all_count = float(len(humans)) or 0.000001
        all_dead_count = 0.000001
        all_pigeon_count = 0.0
        dead_pigeon_count = 0.000001
        for human in humans:
            if human.is_pigeon:
                all_pigeon_count += 1
            if human.if_dead:
                all_dead_count += 1
                if human.is_pigeon:
                    dead_pigeon_count += 1
        dead_percent = round(all_dead_count / all_count * 100)
        dead_pigeon_count = round(dead_pigeon_count / all_dead_count * 100)
        return '{0} humans,{1}% dead, {2}% are pigeon'.format(all_count, dead_percent, dead_pigeon_count)

    def print_info(self):
        print('init status: %s' % self.get_humans_info(self.init_humans))
        print('last status: %s' % self.get_humans_info(self.living_humans))
        print('death: %s' % self.get_humans_death_info(self.all_humans))
