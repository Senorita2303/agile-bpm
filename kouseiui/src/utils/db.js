// Initial database structure
const dbData = [
	{
		dbName: "masterDB",					// Database name
		version: 1,							// Database version number, when the structure changes
		tables: [							// Table
			{
				name: "SYS_favorites",		// Table name
				keyPath: "uid",				// Primary key
				autoIncrement: false,		// Is the primary key auto-incrementing?
				index: [					// Index
					{
						name: "name_index",	// Index name
						key: "name"			// Index key
					}
				]
			},
			{
				name: "SYS_keyword",
				keyPath: "id"
			}
		]
	},
	{
		dbName: "guestDB",
		version: 1,
		tables: [
			{
				name: "MY_demo",
				keyPath: "id"
			}
		]
	}
]

// Usage examples
// import DB from '@/utils/db'

// Initialize and create the database. Usually it is executed when the project starts
// await DB.create()

// Open a database and return the database instance
// const database = await DB.open("dbName")

// Add data to the tablenName table in the open database
// await database.add("tablenName", data)

// Query
// await database.get("tablenName", key)

// Query by index
// await database.indexGet("tablenName", "indexName", indexVal)

// Modify
// await database.put("tablenName", data)

// Delete
// await database.delete("tablenName", key)

// Get all
// await database.getAll("tablenName")

// Clear a table data
// await database.clear("tablenName")

// Get information about a table
// database.getTable("tablenName")

// Get all tables
// database.getTables()

// Close database connection
// database.close()


export default {
	// Create database, table, initial data
	create() {
		var promiseArray = []
		const addDB = db => {
			return new Promise((resolve, reject) => {
				const request = indexedDB.open(db.dbName, db.version)
				request.onupgradeneeded = e => {
					const thisDB = e.target.result
					db.tables.forEach(item => {
						let table = null
						if (thisDB.objectStoreNames.contains(item.name)) {
							// The table already exists, delete the old index
							table = e.target.transaction.objectStore(item.name)
							table.indexNames.length > 0 && table.indexNames.forEach(indexName => {
								table.deleteIndex(indexName)
							})
						} else {
							// Create a new table
							table = thisDB.createObjectStore(item.name, {
								keyPath: item.keyPath,
								autoIncrement: item.autoIncrement
							})
						}
						// Create index
						item.index && item.index.forEach(ind => {
							table.createIndex(ind.name, ind.key, { unique: false })
						})
					})
				},
					request.onsuccess = e => {
						return resolve(e.target.result)
					}
				request.onerror = e => {
					return reject(e)
				}
			})
		}
		dbData.forEach(db => {
			promiseArray.push(addDB(db))
		})
		return new Promise((resolve, reject) => {
			Promise.all(promiseArray).then((e) => {
				resolve(e)
			}).catch(e => {
				reject(e)
			})
		})
	},
	// All databases
	databases() {
		return indexedDB.databases()
	},
	// Open database
	open(dbName) {
		return new Promise((resolve, reject) => {
			const request = indexedDB.open(dbName)
			request.onsuccess = e => {
				const database = new this.database(e.target.result)
				resolve(database)
			}
			request.onerror = e => {
				reject(e)
			}
		})
	},
	// Delete database
	deleteDB(dbName) {
		return indexedDB.deleteDatabase(dbName)
	},
	// Database class
	database: function (IDBDatabase) {
		this.IDBDatabase = IDBDatabase

		/**
		 * Add row data 
		 * @param {string} tableName Table name
		 * @param {object} data Data
		 * @returns {promise}
		 */
		this.add = (tableName, data) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).add(data)
				request.onsuccess = e => {
					resolve(e)
				}
				request.onerror = e => {
					reject(e)
				}
			})
		}

		/**
		 * Modify the row data, if not found, add a new one
		 * @param {string} tableName Table name
		 * @param {object} data Data
		 * @returns {promise}
		 */
		this.put = (tableName, data) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).put(data)
				request.onsuccess = e => {
					resolve(e)
				}
				request.onerror = e => {
					reject(e)
				}
			})
		}

		/**
		 * Delete row
		 * @param {string} tableName Table name
		 * @param {string} key Primary key
		 * @returns {promise}
		 */
		this.delete = (tableName, key) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).delete(key)
				request.onsuccess = e => {
					resolve(e)
				}
				request.onerror = e => {
					reject(e)
				}
			})
		}

		/**
		 * Get rows based on primary key
		 * @param {string} tableName Table name
		 * @param {string} key Primary key
		 * @returns {promise}
		 */
		this.get = (tableName, key) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).get(key)
				request.onsuccess = () => {
					resolve(request.result || null)
				}
				request.onerror = e => {
					reject(e)
				}
			})
		}

		/**
		 * Get row by index
		 * @param {string} tableName Table name
		 * @param {string} indexName Index library name
		 * @param {string} indexVal Index value
		 * @returns {promise}
		 */
		this.indexGet = (tableName, indexName, indexVal) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).index(indexName).get(indexVal)
				request.onsuccess = () => {
					resolve(request.result || null)
				}
				request.onerror = e => {
					reject(e)
				}
			})
		}

		/**
		 * Get all rows
		 * @param {string} tableName Table name
		 * @returns {promise}
		 */
		this.getAll = (tableName) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).getAll()
				request.onsuccess = () => {
					resolve(request.result || null)
				}
				request.onerror = e => {
					reject(e)
				}
			})
		}

		/**
		 * Clear table
		 * @param {string} tableName Table name
		 * @returns {promise}
		 */
		this.clear = (tableName) => {
			return new Promise((resolve, reject) => {
				const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName).clear()
				request.onsuccess = e => {
					resolve(e)
				}
				request.onerror = err => {
					reject(err)
				}
			})
		}

		/**
		 * Get table information
		 * @returns {IDBObjectStore}
		 */
		this.getTable = (tableName) => {
			const request = IDBDatabase.transaction([tableName], 'readwrite').objectStore(tableName)
			return request
		}

		/**
		 * Get all tables
		 * @returns {[IDBObjectStore]}
		 */
		this.getTables = () => {
			const tables = []
			for (let item of IDBDatabase.objectStoreNames) {
				tables.push(IDBDatabase.transaction([item], 'readwrite').objectStore(item))
			}
			return tables
		}

		/**
		 * Close database connection
		 * @returns {}
		 */
		this.close = () => {
			return IDBDatabase.close()
		}
	}
}
