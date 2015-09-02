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

	`PUT /api/book`

data:		book object

response:	none

#####Retrieve existing book
	`GET /api/book/:isbn:/owner`

data:		none

response:	book object

#####Add renter

	`PUT /api/book/:isbn/:owner/addrenter`

data:		user object

response:	none

#####Remove renter

	`PUT /api/book/:isbn/:owner/removerenter`

data:		user object

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
	`GET /api/crowd/:crowdId`

data:		none

response:	crowd object

#####Create crowd
	`POST /api/crowd`

data:		crowd object # PS! _id should be -1

response:	none


#####Add member to crowd

	`PUT /api/crowd/:crowdId/addmember`

data:		user object

response:	none


#####Remove member from crowd

	`PUT /api/crowd/:crowdId/removemember`

data:		user object

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

	`GET /user/:user`

data:		none

response:	user object
