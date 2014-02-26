import urllib2 as urllib
import sys
import json

HOST = 'localhost'
PORT = '9201'
INDEX = 'habr'
TYPE = 'document'
INDEX_URL = 'http://' + HOST + ':' + PORT + '/' + INDEX + '/'

def index_exists():
	request = urllib.Request(INDEX_URL)
	request.get_method = lambda: 'HEAD'
	try:
		urllib.urlopen(request)
	except urllib.HTTPError, e:
		if e.code == 404:
			return False
		else:
			raise e
	return True

def index_create():
	response = urllib.urlopen(INDEX_URL, "")
	return json.loads(response.read())

def put_mapping(data):
	response = urllib.urlopen(INDEX_URL + TYPE + '/_mapping', data)
	return json.loads(response.read())


if not index_exists():
	response = index_create()
	sys.stdout.write(str(response) + '\n')
f = open(TYPE + '.json', 'r')
try:
	response = put_mapping(f.read())
	sys.stdout.write(str(response) + '\n')
except Exception:
	sys.stdout.write(TYPE + ': FAIL\n')




