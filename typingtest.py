import re
from colorama import Fore, Style
from timeit import default_timer as timer
from random import shuffle
from tabulate import tabulate

AVERAGE_WORD_LENGTH = 5
LINE_UP = '\033[1A'
LINE_CLEAR = '\x1b[2K'
RED = "\033[0;31m"
GREEN = "\033[0;32m"
RESET = "\033[0m"


def main():
    word_list = parser('words.txt')
    input("Instructions: hit enter after every word you typed for it to count. \nPress Enter to continue.\n")
    num_of_words = int(input("How many words do you want in the test? "))

    print("\n" *3 + ' '.join(word_list[:num_of_words]), "\n")
    start = timer()
    total, score = tester(word_list, num_of_words)
    stop = timer()

    time_elapsed = stop - start
    give_result(score, time_elapsed, total)


# parse word file
def parser(x):
    with open(x) as file:
        words = file.read().replace('\n', '').strip()
        words = re.sub(r'[0-9]', '', words)
        word_list = words.split()
        shuffle(word_list)
        return word_list


# keeps track of user's score and manipulates CLI to display word correctness
# return total number of characters inputted and number of correctly typed characters
def tester(w, n):
    score = 0
    total = 0
    for i in range(n):
        user_input = input()
        print(LINE_UP, end=LINE_CLEAR)
        if user_input == w[i]:
            score += len(w[i])
            print(LINE_UP * 2 + " " * total + " " * i + GREEN + w[i] + "\n")
        else:
            print(LINE_UP * 2 + " " * total + " " * i + RED + w[i] + "\n")
        total += len(w[i])
    return total, score


# return user test results in table format
def give_result(score, time, total):
    wpm = (score/AVERAGE_WORD_LENGTH) * 60/time
    table = [["WPM", f"{wpm:.2f}"], ["characters", f"{score:}"], ["time elapsed", f"{time:.2f} seconds"], ["accuracy", f"{score * 100/total:.2f}%"]]
    print(RESET + tabulate(table, tablefmt="simple_grid"))


main()