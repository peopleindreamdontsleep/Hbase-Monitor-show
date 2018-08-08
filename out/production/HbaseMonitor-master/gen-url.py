
TEMPLATE = "http://%s:16030/jmx"
PREFIX = "todi"
r = range(1, 49)

ret = []
for i in r:
    url = TEMPLATE % (PREFIX + str(r))
    ret.append(url)

print(",".join(ret))

