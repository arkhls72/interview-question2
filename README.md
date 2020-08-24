
### Part one ( 1 controller: DemoArrayController)

*  save() 			:  Insert array of integer to memory - The order elements of an array ignored when added to memory   
*  get()  			:  Return ID of  array in memory
*  getgetPermutation()  			:  Return a random order of an array by ID 
*  removeMemory()   :  Clear memory. 
 
### Controller detail
 
* Calling end point POST `http://localhost:5000/store?numbers=2,1,3,4,6,5,7` save array to memory
* Calling end point GET `http://localhost:5000/store?numbers=2,1,3,4,6,5,7` returns an ID of the array 
* Calling end point GET `http://localhost:5000/permutation?id=2` returns a random permutation of the array
* Calling end point DELETE `http://localhost:5000/clear/memory` clears the memory.


###  Services ( 1 service: DemoServoce )  
	 
Supports controller's request 

* save(DemoArrayDTO source): add array to memory and return the object back in DTO format DemoArray
* getId(DemoArrayDTO source):   get  ID of an array in DTO format DemoArray if not found throws Exception 
* getArray(DemoArrayDTO source): get ID and array  in DTO format
* getSuffeled(Integer[] numbers):  change the order of array's element inside array randomly
* clearCache(): remove all available element inside casche

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


### In-Memory Cache configuration (CaceheConfig )
 * Includes in-memory spring boot cache  
 
 
  
