
### Part one and two ( 1 controller: DemoArrayController)


*  save() 			:  Insert array of integer to H2.DB and memory.
                       When added to database, the order of elements of an array is ignored. if successfully inserted then will be added to memory    
*  get()  			:  Return ID of array from memory. if not exists then query database , throws exception if not found in DB 
*  getPermutation() :  Return a random order of an array by ID from memory, if not found then query database, it will throw exception if not found in database
*  removeMemory()   :  Clear memory.
* getFromMemory()   : Return ID only from memory
 
 
### Controller detail
 
* Calling end point POST `http://localhost:5000/store?numbers=2,1,3,4,6,5,7` save array to database and memory
* Calling end point GET `http://localhost:5000/store?numbers=2,1,3,4,6,5,7` returns an ID of the array from memory or database, throws exception if not found. 
* Calling end point GET `http://localhost:5000/permutation?id=2` returns a random permutation of the array from memory or database, throws exception if not found. 
* Calling end point GET `http://localhost:5000/memroy?numbers=2,1,3,4,6,5,7`` returns ID from memory or database, throws exception if not found.
* Calling end point DELETE `http://localhost:5000/clear/memory` clears the memory.


###  Services ( 1 service: DemoServoce )  
	 
Supports controller's request 

* save(DemoArrayDTO source): add array to memory and return the object back in DTO format DemoArray
* getId(DemoArrayDTO source):   get  ID of an array in DTO format DemoArray if not found throws Exception 
* getArray(DemoArrayDTO source): get ID and array  in DTO format
* getSuffeled(Integer[] numbers):  change the order of array's element inside array randomly
* clearCache(): remove all available element inside cache

###  Manager ( 1 Manger: DemoCacheManager )
Manages In-Memory cache inquiries
* getDemoCache() : return object cache numberMap
* clearAll(): removes all elements of the cache
* getByKey(): returns array by cache key     
* add(): add new array to cache
* getCacheKey: create a unique hash code key. order of elements are ignored. same content but 2 different order are identical 

 
### Data transfer object DTO DemoArrayDTO and DemoCacheDTO) 
* DemoArrayDTO : matching DemoArray entity  
* DemoCacheDTO : cache response object when has been cleared.

### Repository

DemoArray is an entity model mapping table TBL_DEMO_ARRAY  

*  id : PK : hash-code
*  numbers : CLOB  that holds JSON array of numbers


### Converter 
MapperService converts DTO to entity and vice versa 

### In-Memory Cache configuration (CaceheConfig )
 * Includes in-memory spring boot cache  
 
 
### Integration test  
* DemoSeverBootTestIT

### Unit tests
* DemoApplicationTests
* DemoArrayControllerTest
* DemoServiceTest
* DemoServiceBootTest
* DemoArrayRepositoryTest

  
