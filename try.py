import random
import math
def charge(distance:float,add_fee=0.20):
    if distance<=2.5:
        return 2.5
    else:
        ad=int(math.ceil(5*(distance-2.5)))
        charge=round(ad*add_fee+2.5,2)
        return charge

#lines record the distance of each stations from the beging station.
def creat_line(stations:int):
    lines=[0]*stations
    lines[0]=0
    for i in range(1,len(lines)):
        ad=round(random.uniform(0.3,1.5),2)
        lines[i]=round(lines[i-1]+ad,2)
    return lines

def get_distance(line:list,begin:int,ends:int):
    try:
        if line[ends]-line[begin]>0:
            return round(line[ends]-line[begin],2)
        else:
            return round(line[-1],2)
    except:
        return  round(line[-1],2)

#trips is a list for each trip. it record as the [line name, begin station, end station]
def get_trips_charge(trips:list):
    charges=0
    l_distance=0
    detials=list()
    if len(trips)>=0:
        for line_name, begin_station, end_station in trips:               
            new_distance=get_distance(lines[line_name], begin_station, end_station)
            single_char=round(charge(l_distance+new_distance)-charges,2)
            detials.append([new_distance,single_char])
            charges=charge(l_distance+new_distance)
            l_distance=round(l_distance+new_distance,2)
    print(f"total distance:{l_distance}\ntotal chatges:{charges}\nDetials:")
    for i in range(len(trips)):
        print(f"form:{trips[i][0]}{trips[i][1]} to {trips[i][0]}{trips[i][2]},distance:{detials[i][0]},charge:{detials[i][1]}")
    return detials,l_distance,charges

lines=dict()
lines['a']=creat_line(10)

lines['b']=creat_line(11)

lines['c']=creat_line(8)
for k,v in lines.items():
    print(k,':',v)
trip=[['c',5,0],['a',1,5],['b',0,7],]
get_trips_charge(trip)