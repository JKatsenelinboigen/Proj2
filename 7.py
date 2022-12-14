
def reward(s):
    return 1 if s == 3 else 0

def transitionModel(si, a, sj):
    
    match si, a, sj:
        case 1, 1, 1:
            return 0.2
        case 1, 1, 2:
            return 0.8
        case 1, 2, 1:
            return 0.2
        case 1, 2, 4:
            return 0.8
        case 2, 2, 2:
            return 0.2
        case 2, 2, 3:
            return 0.8
        case 2, 3, 2:
            return 0.2
        case 2, 3, 1:
            return 0.8
        case 3, 4, 2:
            return 1.0
        case 3, 3, 4:
            return 1.0
        case 4, 1, 4:
            return 0.1
        case 4, 1, 3:
            return 0.9
        case 4, 4, 4:
            return 0.2
        case 4, 4, 1:
            return 0.8



def valueIteration(discount: float, threshhold: float) :
    
    v = {s: 0 for s in S}

    r = reward


    converge = False
    while not converge:
        delta = 0

        for s in S:
            temp = v[s]
            v[s] = r(s) + discount * max()

S = [1, 2, 3, 4]
A = [1, 2, 3, 4]