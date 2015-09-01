###Book
#####Object:
```
{
    _id: String,
    isbn: String
    owner: String,
    availableForRent: Integer, 
    rentedTo: Array[string],
    numberOfCopies: Integer
}
```

#####Add new book or update existing book

<<<<<<< HEAD
address:	`PUT /api/book`

=======
	`PUT /api/book`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		book object

response:	none

#####Retrieve existing book
<<<<<<< HEAD

address:	`GET /api/book/:isbn:/owner`
=======
	`GET /api/book/:isbn:/owner`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	book object


#####Add renter

<<<<<<< HEAD
address:	`PUT /api/book/:isbn/:owner/addrenter/:renter`
=======
	`PUT /api/book/:isbn/:owner/addrenter/:renter`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	none


#####Remove renter

<<<<<<< HEAD
address:	`PUT /api/book/:isbn/:owner/removerenter/:renter`
=======
	`PUT /api/book/:isbn/:owner/removerenter/:renter`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	none

***
###Crowd
#####Object:
```
{
    _id: String, 
    name: String,
    creator: String,
    members: Array[String]
}
```

#####Retrieve existing crowd
<<<<<<< HEAD

address:	`GET /api/crowd/:crowdId`
=======
	`GET /api/crowd/:crowdId`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	crowd object


#####Create crowd
<<<<<<< HEAD

address:	`POST /api/crowd`
=======
	`POST /api/crowd`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		name, creator, members

response:	none


#####Add member to crowd

<<<<<<< HEAD
address:	`PUT /api/crowd/:crowdId/addmember/:username`
=======
	`PUT /api/crowd/:crowdId/addmember/:username`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	none


#####Remove member from crowd

<<<<<<< HEAD
address:	`PUT /api/crowd/:crowdId/removemember/:username`
=======
	`PUT /api/crowd/:crowdId/removemember/:username`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	none

***
###User
#####Object:
```
{
	name: String
	memberOf: Array[String] # Crowd._id
	books: Array[String] # PS! book _id, not isbn
}
```

#####Get user

<<<<<<< HEAD
address:	`GET /user/:user`
=======
	`GET /user/:user`
>>>>>>> 36b06f8ca4b9aab848df778acbd3d5e250a98618

data:		none

response:	user object
