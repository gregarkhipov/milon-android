#!/usr/bin/python2.7
import sqlite3, json

with open('dict-he-en.json') as data_file:    
    he_dict = json.load(data_file)

with open('dict-en-he.json') as data_file:    
    en_dict = json.load(data_file)

db = sqlite3.connect('milon.db')
c = db.cursor()

c.execute('''
    CREATE TABLE IF NOT EXISTS android_metadata (
        locale TEXT DEFAULT "en_US"
    );
''')

c.execute('''
    INSERT INTO android_metadata VALUES ("en_US")
''')

c.execute('''
    CREATE TABLE IF NOT EXISTS he_word(
        id INTEGER PRIMARY KEY,
        translated TEXT,
        translation TEXT,
        part TEXT
    )
''')

c.execute('''
    CREATE TABLE IF NOT EXISTS en_word(
        id INTEGER PRIMARY KEY,
        translated TEXT,
        translation TEXT,
        part TEXT
    )
''')

for w in he_dict:
    translation = ''

    for i, t in enumerate(w['translation']):
        if not i == 0:
            translation += ', '
        translation += t
 
    word = [
        w['translated'],
        translation,
        w['part_of_speech']
    ]

    c.execute('INSERT INTO he_word VALUES (NULL, ?, ?, ?)', word)

for w in en_dict:
    translation = ''

    for i, t in enumerate(w['translation']):
        if not i == 0:
            translation += ', '
        translation += t

    word = [
        w['translated'],
        translation,
        w['part_of_speech']
    ]

    c.execute('INSERT INTO en_word VALUES (NULL, ?, ?, ?)', word)
 
db.commit()
db.close()
