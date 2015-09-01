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

address:	`PUT /api/book`
data:		book object
response:	none

#####Retrieve existing book
address:	`GET /api/book/:isbn:/owner`
data:		none
response:	book object

#####Add renter
address:	`PUT /api/book/:isbn/:owner/addrenter/:renter`
data:		none
response:	none

#####Remove renter

address:	`PUT /api/book/:isbn/:owner/removerenter/:renter`
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
address:	`GET /api/crowd/:crowdId`
data:		none
response:	crowd object

#####Create crowd
address:	`POST /api/crowd`
data:		name, creator, members
response:	none


#####Add member to crowd

address:	`PUT /api/crowd/:crowdId/addmember/:username`

data:		none

response:	none


#####Remove member from crowd

address:	`PUT /api/crowd/:crowdId/removemember/:username`

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

address:	`GET /user/:user`

data:		none

response:	user object
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

address:	`PUT /api/book`


data:		book object

response:	none

#####Retrieve existing book

address:	`GET /api/book/:isbn:/owner`

data:		none

response:	book object


#####Add renter

address:	`PUT /api/book/:isbn/:owner/addrenter/:renter`

data:		none

response:	none


#####Remove renter

address:	`PUT /api/book/:isbn/:owner/removerenter/:renter`

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

address:	`GET /api/crowd/:crowdId`

data:		none

response:	crowd object


#####Create crowd

address:	`POST /api/crowd`

data:		name, creator, members

response:	none


#####Add member to crowd

address:	`PUT /api/crowd/:crowdId/addmember/:username`

data:		none

response:	none


#####Remove member from crowd

address:	`PUT /api/crowd/:crowdId/removemember/:username`

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

address:	`GET /user/:user`

data:		none

response:	user object
