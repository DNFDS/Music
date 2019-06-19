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
    listened = sorted(listened, key=lambda x: x[2], reverse=True)[0:num]
    listened_singer = set([x[1] for x in listened])

    all_singers = set(Counter(artist).keys())
    unlistened_singers = all_singers - listened_singer

    result = []
    outputstr = ''

    for element in unlistened_singers:
        prediction = algo.predict(uid=user_id, iid=element)
        result.append([prediction[1], prediction[3]])

    resultTopThree = sorted(result, key=lambda x: x[1], reverse=True)

    if(len(resultTopThree)<num):
        gap=num-len(resultTopThree)
        for element in listened[-1-gap:-1]:
            resultTopThree.append((element[1],element[2]))

    for element in resultTopThree[0:num]:
        outputstr = outputstr + str(element[0]) + ','

    return outputstr