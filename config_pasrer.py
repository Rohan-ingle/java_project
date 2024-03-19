# Python program to read json/txt file

# import pandas as pd
# cfg = pd.read_json('config.json')
# print(cfg['ip'])

#////////////////////////////////////////////////////////////////////////#

import json
def parse_cfg(path = 'config.txt', retrieve =[]):
    retrieve_out=[]
    with open(path,'r') as cfg:
        cfg = json.load(cfg)

    for i in retrieve:
        try:
            retrieve_out.append(cfg[i])
        except KeyError:
            retrieve_out.append(None)

    return retrieve_out

#////////////////////////////////////////////////////////////////////////#

# with open('config.txt','r') as cfg:
#         cfg = json.load(cfg)
#         print(cfg['affafasadfasf'])

# out = parse_cfg(retrieve = ['ip', 'address'])
# print(out)