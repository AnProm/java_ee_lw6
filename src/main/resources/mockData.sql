INSERT INTO artist(name) values ('artist1'), ('artist2'), ('artist3');

INSERT INTO album(name, genre, artist_id) VALUES
    ('album1','genre1', 1),
    ('album2', 'genre2', 2),
    ('album3','genre1', 2);

INSERT INTO composition(name, duration, album_id) VALUES
    ('comp1', 9, 1), ('comp2', 99, 1), ('comp3', 4, 1), ('comp4', 7, 3);

SELECT c.name as composition_name, c.duration as composition_duration,
       a.name as albom_name FROM composition AS c
INNER JOIN album a on a.id = c.album_id
WHERE duration > 4
ORDER BY duration ASC
LIMIT 1