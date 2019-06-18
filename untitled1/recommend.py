from collections import Counter
from surprise import Dataset
from surprise import Reader
from surprise import SVD
import pandas as pd
import sys

user=[int(x) for x in  sys.argv[1].split(',')[0:-1]]
artist=[int (x) for x in sys.argv[2].split(',')[0:-1]]
rating=[int(x) for x in sys.argv[3].split(',')[0:-1]]
user_id=int(sys.argv[4])
def music_recommendation_based_on_singer(user, artist, rating, user_id, num=3):
    min_rating = min(rating)
    max_rating = max(rating)

    ratings_dict = {'itemID': artist,
                    'userID': user,
                    'rating': rating}

    df = pd.DataFrame(ratings_dict)
    reader = Reader(rating_scale=(min_rating, max_rating))
    data = Dataset.load_from_df(df[['userID', 'itemID', 'rating']], reader)

    algo = SVD()

    trainset = data.build_full_trainset()
    algo.fit(trainset)

    listened = [x for x in zip(user, artist, rating) if x[0] == user_id]
    listened = sorted(listened, key=lambda x: x[2], reverse=True)
    listened_singer = set([x[1] for x in listened])

    all_singers = set(Counter(artist).keys())
    unlistened_singers = all_singers - listened_singer

    result = []
    outputstr = ''

    if (len(unlistened_singers) <= num):
        result = [x[1] for x in listened[num:num + num]]
        for element in result:
            outputstr = outputstr + str(element) + ','
        return outputstr

    for element in unlistened_singers:
        prediction = algo.predict(uid=user_id, iid=element)
        result.append([prediction[1], prediction[3]])

    resultTopThree = sorted(result, key=lambda x: x[1], reverse=True)
    outputstr = ''

    for element in resultTopThree[0:num]:
        outputstr = outputstr + str(element[0]) + ','

    return outputstr

print(music_recommendation_based_on_singer(user,artist,rating,user_id))
