import { useEffect, useState } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { BlueCard } from "@/components/BlueCard";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { Checkbox } from "@/components/Checkbox";
import { Modal } from "@/components/Modal";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import {
  useAccountsQuery,
  useCreateAccountMutation,
  useUpdateAccountMutation,
  useDeleteAccountMutation,
  useChangePasswordMutation,
  type Account,
  type CreateAccountRequest,
  type UpdateAccountRequest,
  type ListAccountsParams,
} from "@/features/accounts";
import pfp from "../assets/profile icon.jfif";
import wallet from "../assets/wallet icon.png";
import orange_wallet from "../assets/orange wallet icon.png"




// export const BranchesPage = () =>{
//      const headers =[
//                         {
//                             id: 1,
//                             KEY: "NAME",
//                             Label :"Name"
//                         },
//                         {
//                             id: 2,
//                             KEY: "LOCATION",
//                             Label:"Location"
//                         },
//                         {
//                             id:3,
//                             KEY: "PHONE",
//                             Label: "Phone"
//                         },
//                         {
//                             id: 4,
//                             KEY: "DEL",
//                             Label: "Del"
//                         },
//                         {
//                             id:5,
//                             KEY:"EDIT",
//                             Label:"Edit"
//                         }
//                     ]




                 


//     const [testData,setTestData] =useState([
//     {
//     id:"1" ,
//     NAME: "branch1",
//     LOCATION: "Helwan" ,
//     PHONE:"123123123" ,
//         DEL: "no" ,
//     },
//     {
//         id:"2" ,
//         NAME:"branche2" ,
//         LOCATION:"makram" ,
//         PHONE:"456456456" ,
//         DEL:"yes" ,
//     },
//     {
//         id: "3",
//         NAME:"Branch3" ,
//         LOCATION:"shobra" ,
//         PHONE: "789789798" ,
//         DEL: "no" ,
//     }
//     ])
//     const data = testData
//     const [branchLocation, setBranchLocation] = useState("");
//     const [branchName, setBranchName] = useState("");
//     const [branchPone, setBranchPhone] = useState("");
    
//     function handleAddBranch(){

//         const newBranch ={
//             id: String(testData.length + 1),
//             NAME: branchName,
//             LOCATION: branchLocation,
//             PHONE: branchPone,
//             DEL: "no"
//         }
//         setTestData(testData => [...testData, newBranch])
        

//     }
//     // new branch functions

//     let deleteOptions =["yes","no"];
//     function handleNameChange (event){
//         setBranchName (event.target.value);
//     }

//     function handleLocationChange (event){
//         setBranchLocation (event.target.value);
//     }

//     function handlePhoneChange(event){
//         setBranchPhone (event.target.value);
//     }

//     const[showForm,setShowForm] = useState(false);
//     const showFormFun = (e) => {
//         setShowForm(true)
//     }
//     const hideFormFun = (e) =>{
//         setShowForm(false)
//     }

//     // filter functions
//     const handleHideDel= (e) =>{
//         if(deletedFilter){
//             setDeletedFilter(false)
//             setHideDel(["yes"])
//         }else{
//             setHideDel(["no"])
//             setDeletedFilter(true)
//         }


//     }

//    const handleRemoveItem= (index) =>{
//     setListBrances(b=>b.filter((element,i)=> i !==index));
//     console.log("this should work")

//    };


//    interface Account extends BranchResponse {}

// interface BranchResponse {
//   id: string;
//   NAME: string;
//   LOCATION: string;
//   PHONE: string;
//   DEL: string;}

//     interface EditingBranch {
//       id: string;
//       NAME: string;
//       LOCATION: string;
//       PHONE: string;
//       DEL: string;
//     }



    
//       const [editForm, setEditForm] = useState<EditingBranch>({
//         id: "" ,
//         NAME: "",
//         LOCATION: "",
//         PHONE: "",
//         DEL: "",
//         });



// const [editingId,setEditingId] = useState ()




//     const handleEditClick = (account: Account) => {
//     setEditingId(account.id);
//     setEditForm({
//       id: account.id,
//       NAME: account.NAME,
//       LOCATION: "",
//       PHONE: account.PHONE,
//       DEL: account.DEL ?? "no",
//     });
//     // setFormErrors({});
//   };


//     const locations = [{value:"helwan" ,label:"helwan"} ,{
//          value: "makram" ,label:"makram"},
//          {value: "shobra", label:"shobra"},
//          {value: "rehab", label: "rehab"}];
//     const [deletedFilter, setDeletedFilter] = useState(false);
//     const [branchSearch,setBranchSearch] = useState ("");
//     const [listBranches,setListBrances] = useState(testData);
//     const [hideDel,setHideDel] = useState(["no"])
//     const [branchLocationFilter,setBranchLocationFilter] = useState([])
//     const [filteredLocation,setFilteredLocation] = useState(testData)


//      const handleLocationFilter =(e) =>{
//         setBranchLocationFilter(e.target.value)
//     }

//     useEffect(()=>{
//  filterLocation();
//  console.log("it's still broken but new now")
//     },[setBranchLocationFilter])

//     const filterLocation = ()=>{
//         if(branchLocationFilter.length >0 ){
//             let tempLocation = branchLocationFilter.map((locations)=>{
//              let tempData = filteredLocation.filter((data)=> data.LOCATION ===locations)
//             setFilteredLocation(tempData)
//              return tempData}
//             )
            
//         }
//     }

// useEffect(()=>{
//     hideDeleted();
// },hideDel)
//     const hideDeleted = () => {
//         if(!deletedFilter){
            
//             let tempDel = "yes" 
//             let tempData = filteredLocation.filter((data) =>data.DEL !== tempDel);
//             setListBrances(tempData)
//         }
//         else{
//             setListBrances(filteredLocation)
//         };
      
//     }

//     return(
        
//         <main className="flex-1 p-6 lg:p-10 flex flex-col gap-8">
//             <Header
//              title="Branch management"
//              subtitle="add and update branch info"
//              actions ={
//                 <Button
//                 variant="primary"
//                 size="sm"
//                 onClick={showFormFun}
//                 >Add New Branch+</Button>
//              }
//              >

//              </Header>
//              {/* new branch form */}
//              {showForm && <Card className="newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
//                 <Header className="font-headline-md text-headline-md text-on-background mb-6" title="Add new branch"></Header>
//                 <form>
//                     <div>
//                         <label>Branch Name</label>
//                         <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Branch Name"
//               placeholder="example name" value={testData.NAME} onChange={handleNameChange} type ="text"></input></div>
                
//                 <div>
//                     <label>Branch Location</label>
//                     <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="example Place"
//                  type ="text" value={testData.LOCATION} onChange={handleLocationChange}></input></div>

//                <div> <label>Phone number</label>
//                <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="PhoneNum"
//               type ="text" value={testData.PHONE} onChange={handlePhoneChange}></input></div>
              
//                <div className="flex gap-3 pt-2">
//                  <Button
//                 type="submit"
//                 variant="primary"
//                 size="md"
//                 onClick={handleAddBranch}>Add Branch</Button>
//                 <Button
//                 type="button"
//                 variant="outline"
//                 size="md"
//                 onClick={hideFormFun}>Cancle</Button>
//                </div>
              
//               </form>
//              </Card>}
//              {/* filter branches */}
//              <Card>
//                 <div className = "mb-6">
//                     <h3 className="font-headline-md text-headline-md text-on-background mb-1">
//                         Filter Branches
//                     </h3>
//                      <p className="text-body-sm text-on-surface-variant">
//                       Number of branches : {listBranches.length}
//                     </p>
//                 </div>
//                 <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
//                     <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
//                          <div className="col-span-1 md:col-span-4">
//                             <label>search branch name</label>
//                             <Input type="text" value={branchSearch} placeholder="branch name" onChange={(e) =>setBranchSearch(e.target.value)}></Input>
//                          </div>

//                          <div className="col-span-1 md:col-span-4">
//                             <label>Search branch location</label>
//                             <Select
//                             onChange={(e)=>{handleLocationFilter}}
//                             options={locations}
//                             ></Select>
//                          </div>
//                          <div className= "col-span-1 md:col-span-2 flex items-center">
//                              <Checkbox
//                                 label="تضمين المحذوف"
//                                 checked={deletedFilter}
//                                 size="3xl"
//                                 onChange={(e) => {
//                                   handleHideDel(e);
//                                 }}
//                               />
//                          </div>

//                           <div className="col-span-1 md:col-span-2 flex items-end">
//                             <Button
//                               variant="outline"
//                               size="md"
//                               fullWidth
//                               onClick={() => {
//                                 setBranchSearch("");
//                               //   setRoleFilter("");
//                               //   setPage(0);
//                               }}
//                             >
//                               إعادة تعيين الفلاتر
//                             </Button>
//                         </div>
//                     </div>
//                 </div>
//              </Card>

//                 {/* list branches */}
//              <Card>
                
//                  <div className = "mb-6">
//                     <h3 className="font-headline-md text-headline-md text-on-background mb-1">
//                         Branches
//                     </h3>
//                  </div>
//                  <table className="w-full">
                   
//                      <thead>
//                         <tr className="border-b border-outline-variant">

                                

//                             <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                                 Name
//                             </th>
//                             <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                               location
//                             </th>
//                             <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                               Phone number
//                             </th>
//                             <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                               محذوف
//                             </th>
//                             <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                               Edit
//                             </th>
//                         </tr>
//                      </thead>
//                      <tbody>
//
//                         {listBranches.filter((data, index) =>{
//                             return branchSearch.toLowerCase() === "" ? data : data.NAME.toLowerCase().includes(branchSearch)
//                         }).map((data) =>(
//                             <tr 
                            
//                              key={data.id}
//                               className="border-b border-surface-variant hover:bg-surface-container-low transition">

                                    
                                
//                                 <td className="p-4">
//                                    {editingId === data.id ? (
//                                     <Input
//                                       type="email"
//                                       value={editForm.NAME}
//                                       onChange={(e) =>
//                                         setEditForm({
//                                           ...editForm,
//                                           NAME: e.target.value,
//                                         })
//                                       }
//                                       className="text-body-sm"
//                                     />
//                                   ) : (
//                                     <p className="text-body-md text-on-surface">
//                                       {data.NAME}
//                                     </p>
//                                   )}
//                                 </td>

//                                 <td className="p-4">
//                                    {editingId === data.id ? (
//                                       <Input
//                                         type="text"
//                                         value={editForm.LOCATION}
//                                         onChange={(e) =>
//                                           setEditForm({
//                                             ...editForm,
//                                             LOCATION: e.target.value,
//                                           })
//                                         }
//                                         className="text-body-sm"
//                                       />
//                                     ) : (
//                                       <p className="text-body-md text-on-surface">
//                                         {data.LOCATION}
//                                       </p>
//                                     )}
//                                 </td>

//                                      <td className="p-4">
//                                       {editingId === data.id ? (
//                                     <Input
//                                       type="text"
//                                       value={editForm.PHONE}
//                                       onChange={(e) =>
//                                         setEditForm({
//                                           ...editForm,
//                                           PHONE: e.target.value,
//                                         })
//                                       }
//                                       className="text-body-sm"
//                                     />
//                                   ) : (
//                                   <span className= " p-2 rounded-full bg-secondary-container text-on-primary">{data.PHONE} </span>
//                                   )}
//                                 </td>
//                                 <td
//                                  className="p-4">
//                                    <span
//                         className={`px-4 py-1 rounded-full ${data.DEL =="yes" ? "bg-error text-on-error" : "bg-surface-variant text-on-surface-variant"}`}
//                       > {data.DEL =="yes" ? "نعم" : "لا"}</span>
//                                 </td>


                                    
// <td className="p-4">
//                       <div className="flex gap-2">
//                         {editingId === data.id ? (
//                           <>
//                             <Button
//                               variant="primary"
//                               size="sm"
//                             //   onClick={handleEditAccount}
//                             //   disabled={updateMutation.isPending}
//                             //   isLoading={updateMutation.isPending}
//                             >
//                               حفظ
//                             </Button>
//                             <Button
//                               variant="outline"
//                               size="sm"
//                             //   onClick={() => {
//                             //     setEditingId(null);
//                             //     setFormErrors({});
//                             //   }}
//                             >
//                               إلغاء
//                             </Button>
//                           </>
//                         ) : (
//                           <>
//                            <Button
//                                     variant="secondary"
//                                     size="sm"
//                                      onClick={() => handleEditClick(data)}>Edit</Button>
//                              <Button
//                                     variant="outline"
//                                     size="sm"
//                                     onClick={()=> handleRemoveItem(data.id)}>delete</Button>
//                           </>
//                         )}
//                       </div>
//                     </td>
//                               </tr>
//                         ))}


//                      </tbody>
                   
//                  </table>
//              </Card>
//         </main>
//     )
// }

export const BranchesPage = () =>{


 //test data arrays


  const transactionHeader=[
    {
      id:1,
      key:"AMOUNT",
      label:"Amount"
    },
    {
      id:2,
      key:"TYPE",
      label:"Type"
    },
    {
      id:3,
      key:"SHIPMENT_ID",
      label:"ShipmentId"
    },
    {
      id:4,
      key:"TIME",
      label:"Time"
    }
  ]

 

const InventoryHeaders =[
  {
    id:1,
    key: "NAME",
    label:"Name"
  },
  {
    id:2,
    key:"TYPE",
    label:"Type"
  },
  {
    id:3,
    key:"STOCK",
    label:"Stock"
  },
  {
    id:4,
    key:"BRANCH",
    label:"Branch"
  }
]

 const shipmentHeader=[
    {
      id:1,
      key: "SHIPMENT_ID",
      label: "ShipmentId"
    },
    {
      id:2,
      key: "NOTES",
      label: "Notes"
    },
    {
      id:3,
      key: "LAST_UPDATE",
      label: "Last_Update"
    },
    {
      id:4,
      key: "STATUS",
      label: "Status"
    },
    {
      id:5,
      key: "TOTAL_PRICE",
      label: "Total_Price"
    },
    {
      id:6,
      key: "SHIPPING_FEE",
      label: "Shipping_Fee"
    },
    {
      id:7,
      key: "DATE_DELIVERED",
      label: "Date_Delivered"
    }
  ]


const [shipments,setShipments] = useState([
  {
    id:"1",
    SHIPMENT_ID:"132",
    NOTES: "no notes",
    LAST_UPDATE: "10:10 20/2/2022",
    STATUS: "Delivered",
    TOTAL_PRICE: "100$",
    SHIPPING_FEE:"20$",
    DATE_DELIVERED:"20/2/2022"
  },
  {
    id:"2",
    SHIPMENT_ID:"465",
    NOTES: "Handle with care",
    LAST_UPDATE: "10:10 20/3/2022",
    STATUS: "pending pickup",
    TOTAL_PRICE: "109$",
    SHIPPING_FEE:"24$",
    DATE_DELIVERED:""
  },
  {
    id:"3",
    SHIPMENT_ID:"159",
    NOTES: "Do not bend",
    LAST_UPDATE: "10:10 15/2/2022",
    STATUS: "arrived at warehouse",
    TOTAL_PRICE: "300$",
    SHIPPING_FEE:"30$",
    DATE_DELIVERED:""
  },
  {
    id:"4",
    SHIPMENT_ID:"753",
    NOTES: "Fragile",
    LAST_UPDATE: "10:10 20/2/2022",
    STATUS: "Delivered",
    TOTAL_PRICE: "1020$",
    SHIPPING_FEE:"200$",
    DATE_DELIVERED:"20/2/2022"
  },
])

 const [transactions,setTransactions] = useState([
    {
      id:"1",
      AMOUNT:"700",
      TYPE:"Debit",
      SHIPMENT_ID:"123",
      TIME:"10:00 20/3/2024"
    },{
      id:"2",
      AMOUNT:"500",
      TYPE:"Credit",
      SHIPMENT_ID:"456",
      TIME:"14:15 12/5/2025"
    },{
      id:"3",
      AMOUNT:"1000",
      TYPE:"Debit",
      SHIPMENT_ID:"789",
      TIME:"02:00 10/10/2026"
    },{
      id:"4",
      AMOUNT:"750",
      TYPE:"Credit",
      SHIPMENT_ID:"147",
      TIME:"08:40 02/02/2028"
    },
  ])

  const [testInventory,setTestInventory] = useState([
  {
    id:"1",
    NAME:"Phone1",
    TYPE:"Phone",
    STOCK:"10",
    BRANCH:"helwan,rehab"
  },
  {
    id:"2",
    NAME:"Phone2",
    TYPE:"Phone",
    STOCK:"17",
    BRANCH:"giza,tagamo3"
  },
  {
    id:"3",
    NAME:"Watch1",
    TYPE:"watch",
    STOCK:"7",
    BRANCH:"helwan,shobra"
  },
  {
    id:"4",
    NAME:"headphones1",
    TYPE:"headphones",
    STOCK:"27",
    BRANCH:"helwan,giza,shobra"
  }
])

const [merchantId,setMerchantId] = useState("")
const [merchantName,setMerchantName] = useState("")
const [merchantEmail,setMerchantEmail] = useState("")
const [merchantBranch,setMerchantBranch] = useState("")
const [merchantPhone,setMerchantPhone] = useState("")
const [merchantInfo,setMerchantInfo] = useState([
  {
    id:"1",
    MERCHANT_ID: "1222333",
    NAME:"",
    EMAIL:"",
    MERCHANT_BRANCH:"",
    MERCHANT_PHONE:""
  }
])



//shipment variables
const [shipmentId,setShipmentId] = useState("")
const [shipmentNotes,setShipmentNotes] = useState("")
const [shipmentLastUpdated,setShipmentLastUpdated] = useState("")
const [shipmentStatus,setShipmentStatus] = useState("")
const [totalPrice,setTotalPrice]= useState("")
const [shippingFee,setShippingFee] = useState("")
const [dateDelivered,setDateDelivered]=useState("")
const [newShipment,setNewShipment] = useState("")
const [showShipmentForm,setShowShipmentForm] = useState(false)


//inventory variables
const [inventroyName,setInventoryName] = useState("")
const [inventoryType,setInventoryTybe] = useState("")
const [inventoryStock,setInventoryStock] = useState("")
const [inventoryAvailbleAt,setInventoryAvailbeAt] = useState("")
const [newInventory,setNewInventory] = useState([])
const [showInventoryForm,setShowInventoryForm] = useState(false)

//Transactions variables

const [transactionAmount,setTransactionAmount] =useState("")
const [transactionType,setTransactionType] = useState("")
const [transactionShippingId,setTransactionShippingId] = useState("")
const [transactionTime,setTransactionTime] = useState("")
const [newTransaction,setNewTransaction] = useState([])
const [showTransactionForm,setShowTransactionForm] = useState(false)


//transaction functions

function handleNewTransactionAmount (e){
  setTransactionAmount(e.target.value)
}


function handleNewTransactionType (e){
  setTransactionType(e.target.value)
}
function handleNewTransactionShippingId (e){
  setTransactionShippingId(e.target.value)
}
function handleNewTransactionTime (e){
  setTransactionTime(e.target.value)
}

function addNewTransaction(){
  const oldTransactions = transactions
  const tempTransactions={
    id: String(oldTransactions.length + 1),
    AMOUNT: transactionAmount,
    TYPE: transactionAmount,
    SHIPMENT_ID: transactionShippingId,
    TIME: transactionTime
  }
  setNewTransaction(tempTransactions)

  setTransactions(transactions=> [...transactions,newTransaction])

}

function showTransactionFormFun(){
  setShowTransactionForm(true)
}

function hideTransactionFormFun(){
  setShowTransactionForm(false)
}

//shipment functions

function handleNewShipmentId (e){
  setShipmentId(e.target.value);
}

function handleNewShipmentNotes (e){
  setShipmentNotes(e.target.value);
}

function handleNewShipmentLastUpdated (e){
  setShipmentLastUpdated(e.target.value);
}

function handleNewShipmentStatus (e){
  setShipmentStatus(e.target.value);
}

function handleNewShipmentTotalPrice (e){
  setTotalPrice(e.target.value);
}

function handleNewShipmentShippingFee (e){
  setShippingFee(e.target.value);
}

function handleNewShipmentDateDelivered (e){
  setDateDelivered(e.target.value);
}

function showShipmentFormFun (){
  setShowShipmentForm(true)
}

function hideShipmentFormFun (){
  setShowShipmentForm(false)
}

function handleNewShippment (){
  const oldShippment =shipments
  const tempNewShipment ={
id: String(oldShippment.length + 1),
SHIPMENT_ID: shipmentId,
NOTES: shipmentNotes,
LAST_UPDATED: shipmentLastUpdated,
STATUS: shipmentStatus,
TOTAL_PRICE: totalPrice,
SHIPPING_FEE: shippingFee,
DATE_DELIVERED: dateDelivered
  }
setNewShipment(tempNewShipment)

setShipments(shipments=> [...shipments,newShipment])
}

// merchant functions
const [showMerchantForm,setShowMerchantForm] =useState(false)

function handleMerchantBranchChange (event){
  setMerchantBranch(event.target.value);
}

function handleMerchantNameChange (event){
         setMerchantName (event.target.value);
     }


function handleMerchantEmailChange (event){
         setMerchantEmail (event.target.value);
     }     
     
function handleMerchantPhoneChange (event){
         setMerchantPhone (event.target.value);
     }

const showMerchantFormFun = (e) => {
         setShowMerchantForm(true)
     }

 const hideMerchantFormFun = (e) =>{
         setShowMerchantForm(false)
    }

function handleUpdateMerchantInfo(){
  const oldMerchant = merchantInfo
  const tempMerchant={
    id:`${oldMerchant[0]}`,
    MERCHANT_ID:`${oldMerchant[1]}`,
    NAME: merchantName,
    MERCHANTBRANCH: merchantBranch,
    MERCHANTPHONE: merchantPhone
  }

  setMerchantInfo(tempMerchant)
}

//inventory functions

function handleAddInventory(){
  const oldInventory = testInventory
  const tempInventory={
    id: String(oldInventory.length + 1),
    NAME:inventroyName,
    TYPE: inventoryType,
    STOCK: inventoryStock,
    BRANCH: inventoryAvailbleAt
  }

  setNewInventory(tempInventory)

  setTestInventory(testInventory=> [...testInventory,newInventory])
}

function handleNewInventoryName(e) {
  setInventoryName(e.target.value)
}

function handleNewInventoryType(e) {
  setInventoryTybe(e.target.value)
}

function handleNewInventoryStock(e) {
  setInventoryStock(e.target.value)
}

function handleNewInventoryAvailbleAt(e) {
  setInventoryAvailbeAt(e.target.value)
}

function showInventoryFormFun(){
  setShowInventoryForm(true)
}

function hideInventoryFormFun(){
  setShowInventoryForm(false)
}



 


    //MRCHANT PROFILE PAGE

 return(


  <main>
   <Card className=" h-30 mt-9 w-98/100 ml-6 mr-4 "><span>
       <div className="flex">
    <Button onClick={showMerchantForm} className="bg-primary-container p-10 mt-3 mr-4 mb-9">تحديث بيانات</Button>
         <div><p className="mr-100 text-2xl">اسم التاجر</p></div>
         <div> <p className="mr-30 mt-10">رقم التاجر;123465 </p></div>
         <div><img src={pfp} alt="profile pic" className="h-20 mr-7" ></img></div>
       </div>


   {showMerchantForm && <Card className=" mb-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
                  <Header className="font-headline-md text-headline-md text-on-background mb-6" title="تحديث بيانات التاجر"></Header>
                  <form>
                      <div>
                          <label>اسم التاجر</label>
                          <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Branch Name"
                placeholder="مثال اسم" value={merchantInfo.NAME} onChange={handleMerchantNameChange} type ="text"></input></div>
                
                  <div>
                      <label>بريد الالكتروني</label>
                      <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="بريد الالكتروني"
                   type ="text" value={merchantInfo.EMAIL} onChange={handleMerchantEmailChange}></input></div>

                 <div> <label>فرع التاجر</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="ميثال فرع"
                type ="text" value={merchantInfo.MERCHANT_BRANCH} onChange={handleMerchantBranchChange}></input></div>

                 <div> <label>رقم الهاتف</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="رقم الهاتف"
                type ="text" value={merchantInfo.MERCHANT_PHONE} onChange={handleMerchantPhoneChange}></input></div>
              
                 <div className="flex gap-3 pt-2">
                   <Button
                  type="submit"
                  variant="primary"
                  size="md"
                  onClick={handleUpdateMerchantInfo}>تحديث البينات</Button>
                 <Button
                  type="button"
                  variant="outline"
                  size="md"
                  onClick={hideMerchantFormFun}>الغا</Button>
                 </div>
                 </form>
                 </Card>}

        
       </span></Card>
     <span className="flex  gap-4.5 mt-6 w-full">



       <Card className=" grid grid-cols-2 w-75/100 mr-4">
       
<div>

<p className="text-2xl">بيانات التاجر</p>

       <div className="flex">
         <p className="ml-50">اسم التاجر</p>
         <p className="mr-20">البريد الالكتروني</p>
         </div>
         <div className="flex mt-4">
         <p className="bg-gray-200 p-3 rounded-md ml-54">ميثالرقم الهاتف</p>
         <p className="bg-gray-200 p-3 rounded-md">بريد الالكتروني</p>
         </div>
         <div className=" flex mt-5">
           <p className="ml-50">موقع الفرع</p>
           <p className="mr-20">رقم الهاتف</p>
         </div>
         <div className="flex mt-5">
           <p className="bg-gray-200 p-3 w-30 rounded-md ml-50">ميثال فرع</p>
           <p className="bg-gray-200 w-30 p-3 rounded-md">ميثال فرع</p>
         </div>

</div>




<div>


  
       <div className="mr-20 mt-6 mb-3 w-90 ">
<div className="bg-surface-container-lowest p-md rounded-xl border border-outline-variant shadow-sm space-y-md">
<h3 className="text-xl font-semibold p-3  text-headline-xl text-primary">توزيع الشحنات لكل فرع</h3>
<div className="h-48 flex items-end justify-between px-md gap-sm">
<div className="flex-1 bg-secondary-container/20 rounded-t-lg mr-2 ml-2 relative group h-[80%]">
<div className="absolute bottom-0 left-0 right-0 bg-secondary rounded-t-lg h-[70%] group-hover:h-[85%] transition-all"></div>
<span className="absolute -bottom-7 left-1/2 -translate-x-1/2 font-label-sm text-xl text-on-surface-variant">حلوان</span>
</div>
<div className="flex-1 bg-secondary-container/20 rounded-t-lg mr-2 ml-2 relative group h-[80%]">
<div className="absolute bottom-0 left-0 right-0 bg-secondary rounded-t-lg h-[40%] group-hover:h-[55%] transition-all"></div>
<span className="absolute -bottom-7 left-1/2 -translate-x-1/2 font-label-sm text-xl text-on-surface-variant">جيزه</span>
</div>
<div className="flex-1 bg-secondary-container/20 rounded-t-lg mr-2 ml-2 relative group h-[80%]">
<div className="absolute bottom-0 left-0 right-0 bg-secondary rounded-t-lg h-[90%] group-hover:h-[95%] transition-all"></div>
<span className="absolute -bottom-7 left-1/2 -translate-x-1/2 font-label-sm text-xl text-on-surface-variant">شوبره</span>
</div>
<div className="flex-1 bg-secondary-container/20 rounded-t-lg mr-2 ml-2 relative group h-[80%]">
<div className="absolute bottom-0 left-0 right-0 bg-secondary rounded-t-lg h-[30%] group-hover:h-[45%] transition-all"></div>
<span className="absolute -bottom-7 left-1/2 -translate-x-1/2 font-label-sm text-xl text-on-surface-variant">تجمع</span>
</div>
<div className="flex-1  bg-secondary-container/20 rounded-t-lg mr-2 ml-2 relative group h-[80%]">
<div className="absolute bottom-0 left-0 right-0 bg-secondary rounded-t-lg h-[55%] group-hover:h-[70%] transition-all"></div>
<span className="absolute -bottom-7 left-1/2  -translate-x-1/2 font-label-sm text-xl text-on-surface-variant">رحاب</span>
</div>
</div>
</div>
</div>
</div>

       </Card>








       <BlueCard className="w-25/100 ml-3 pl-3"><div className="flex">
         <img className="h-16 ml-3 " src={orange_wallet} alt="wallet icon"></img>
         <p className="text-white mt-1 mr-10">الرصيض الحاللي<br></br>
         13232132$</p></div>
         <Button className="w-90/100 mr-1 mt-4 pl-3">موعامله جديده</Button>
         <Button variant="outline" className="bg-gray-500 text-white w-90/100 mr-1 mt-4 pl-3">المعاملت الصابقه</Button>
       </BlueCard>
     </span>

  
   {showTransactionForm && <Card className=" mb-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
                   <Header className="font-headline-md text-headline-md text-on-background mb-6" title="اضافت معامله جديده"></Header>
                   <form>
                       <div>
                           <label>قيمت المعامله</label>
                           <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Name"
                 placeholder="Amount" value={transactions.AMOUNT} onChange={handleNewTransactionAmount} type ="text"></input></div>
              
                   <div>
                       <label>نوع المعامله</label>
                       <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="Type"
                    type ="text" value={transactions.TYPE} onChange={handleNewTransactionType}></input></div>

                  <div> <label>رقم شحنت المعامله</label>
                  <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="shipment id"
                 type ="text" value={transactions.SHIPMENT_ID} onChange={handleNewTransactionShippingId}></input></div>

                  <div> <label>وقت المعامله</label>
                  <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="time"
                 type ="text" value={transactions.TIME} onChange={handleNewTransactionTime}></input></div>
              
                  <div className="flex gap-3 pt-2">
                    <Button
                   type="submit"
                   variant="primary"
                   size="md"
                   onClick={addNewTransaction}>تحديث البينات</Button>
                   <Button
                   type="button"
                  variant="outline"
                   size="md"
                   onClick={hideTransactionFormFun}>الغا</Button>
                  </div>
                  </form>
                  </Card>}
  

      {showInventoryForm && <Card className=" mb-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
                   <Header className="font-headline-md text-headline-md text-on-background mb-6" title="اضافت منتاج جديد"></Header>
                   <form>
                       <div>
                           <label>اسم المنتج</label>
                           <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Name"
                 placeholder="example name" value={testInventory.NAME} onChange={handleNewInventoryName} type ="text"></input></div>
            
                   <div>
                       <label>نوع المنتاج</label>
                       <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="Type"
                   type ="text" value={testInventory.TYPE} onChange={handleNewInventoryType}></input></div>
                <div> <label>مخزون المنتاج</label>
                  <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="Stock"
                 type ="text" value={testInventory.STOCK} onChange={handleNewInventoryStock}></input></div>

                  <div> <label>متوافر عند</label>
                  <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="Branch"
                 type ="text" value={testInventory.BRANCH} onChange={handleNewInventoryAvailbleAt}></input></div>
              
                  <div className="flex gap-3 pt-2">
                    <Button
                   type="submit"
                   variant="primary"
                   size="md"
                   onClick={handleAddInventory}>اضافت المنتاج</Button>
                   <Button
                   type="button"
                   variant="outline"
                   size="md"
                   onClick={hideInventoryFormFun}>الغا</Button>
                  </div>
                  </form>
                  </Card>}



         {showShipmentForm && <Card className=" mb-6 mt-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
                  <Header className="font-headline-md text-headline-md text-on-background mb-6" title="اضافت شحنه جديده"></Header>
                  <form>
                      <div>
                          <label>رقم الشحنه</label>
                          <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Shipment id"
                placeholder="رقم الشحنه" value={shipments.SHIPMENT_ID} onChange={handleNewShipmentId} type ="text"></input></div>
              
                  

                 <div> <label>اخر تحديث</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="اخر تحديث"
                type ="text" value={shipments.LAST_UPDATE} onChange={handleNewShipmentLastUpdated}></input></div>

                 <div> <label>الحاله</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="الحاله"
                type ="text" value={shipments.STATUS} onChange={handleNewShipmentStatus}></input></div>

                <div> <label>التمن الكلي</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="التمن الكلي"
                type ="text" value={shipments.TOTAL_PRICE} onChange={handleNewShipmentTotalPrice}></input></div>

                <div> <label>رسوم الشحن</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="رسوم الشحن"
                type ="text" value={shipments.SHIPPING_FEE} onChange={handleNewShipmentShippingFee}></input></div>

                <div> <label>تاريخ الوصول</label>
                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="تاريخ الوصول"
                type ="text" value={shipments.DATE_DELIVERED} onChange={handleNewShipmentDateDelivered}></input></div>

                <div>
                      <label>  ملحضات التعباء</label>
                      <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg h-50" label="location" placeholder= " ملحضات التعباء"
                   type ="text" value={shipments.NOTES} onChange={handleNewShipmentNotes}></input></div>


                   <div>
                      <label > ملحضات الشحن</label>
                      <input className="m-1 p-2 w-full border bg-surface-container-low h-50 rounded-lg" label="location" placeholder="ملحضات الشحن"
                   type ="text" value={shipments.NOTES} onChange={handleNewShipmentNotes}></input></div>


              
                 <div className="flex gap-3 pt-2">
                   <Button
                  type="submit"
                  variant="primary"
                  size="md"
                  onClick={handleNewShippment}>اضافت الشحنه</Button>
                  <Button
                  type="button"
                  variant="outline"
                  size="md"
                  onClick={hideShipmentFormFun}>الغا</Button>
             </div>
             </form>
             </Card>}

   <div className="flex w-full ml-4">
      <Card className="mt-6 mr-4">
       <table className=" mr-2 ml-4">
           <thead>
             <tr className="border-b border-outline-variant">                       
                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                       الاسم
                 </th>
                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  النوع
                 </th>
                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                   المخزون
                 </th>
                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                   متوافر عند
                 </th>
                 {/* <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                   Add product
                 </th> */}

                 <Button>تصفح المزيد</Button>
             </tr>
           </thead>
           <tbody>
             {testInventory.map((data)=>(

                <tr 
                          
                 key={data.id}
                 className="border-b border-surface-variant hover:bg-surface-container-low transition">
                  

                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.NAME}
                                      </p></td>
                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.TYPE}
                                      </p></td>
                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.STOCK}
                                      </p></td>
                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.BRANCH}
                                      </p></td>
                </tr>
              
             ))}
            
           </tbody>
       </table>
     </Card>



       <Card className="mr-3 mt-6">
       <table className="w-145 ">
           <thead>
             <tr className="border-b border-outline-variant">                       
                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                       رقم الشحنه
                 </th>
                 
                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                   الملاحضات
                 </th>

                  {/* <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md"> */}
                   {/* Last Updated */}
                 {/* </th>  */} 

                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                   الحاله
                 </th>
                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                   السعر الكلي
                 </th>
                
                  {/* <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md"> */}
                   {/* Shipping Fee */}
                 {/* </th>  */}
                  {/* <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md"> */}
                   {/* Date Delivred */}
                 {/* </th>  */}
                

                 <Button onClick={showShipmentFormFun} className="">تصفح المزيد</Button>
             </tr>
           </thead>
           <tbody>
             {shipments.map((data)=>(

                <tr 
                            
                 key={data.id}
                 className={`border-b border-surface-variant hover:bg-surface-container-low transition  ${data.STATUS ==="Delivered"? "bg-green-200! hover:bg-green-300!" :"" }`}>
                  

                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.SHIPMENT_ID}
                                      </p></td>
                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.NOTES}
                                      </p></td>
                   {/* <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.LAST_UPDATE}
                                      </p></td> */}
                  <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.STATUS}
                                      </p></td>
                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.TOTAL_PRICE}
                                      </p></td>
                   {/* <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.SHIPPING_FEE}
                                      </p></td>
                   <td className="p-4"><p className="text-body-md text-on-surface">
                                        {data.DATE_DELIVERED}
                                      </p></td> */}
                </tr>
              
             ))}
            
           </tbody>
       </table>
       </Card> 
</div>

{/* <div className="bg-primary p-md rounded-xl shadow-md flex items-center gap-md relative overflow-hidden"> */}
{/* <div className="absolute top-0 left-0 w-32 h-32 bg-secondary/10 rounded-full -translate-x-1/2 -translate-y-1/2"></div>
<div className="z-10 space-y-sm flex-1">
<h3 className="font-headline-md text-headline-md text-on-primary">خريطة التوسع القادمة</h3>
<p className="font-body-md text-body-md text-on-primary/80">نحن نخطط لافتتاح 4 فروع جديدة في منطقة البحر الأحمر خلال الربع القادم من العام.</p>
<button className="bg-secondary text-on-secondary px-md py-sm rounded-lg font-label-md hover:bg-secondary/90 transition-all">عرض التوقعات</button>
</div> */}
{/* </div> */}

  
</main>

 )}
    
 


   //transaction history page

{/* 
//   return(
//  <main>

//    <Card className=" h-30 mt-9 w-full ml-6 mr-6 "><span>
//        <div className="flex">
//         <Button className="bg-primary-container p-10 mt-3 mr-4 mb-9">العوده الي صفحت التاجر</Button>
//          <div><p className="mr-100 text-2xl">اسم التاجر</p></div>
//          <div> <p className="mr-30 mt-10">رقم التاجر;123465 </p></div>
//          <div><img src={pfp} alt="profile pic" className="h-20 mr-7" ></img></div>
//        </div>
        
//       </span></Card>

//       <div><p className="mt-6 mr-140 text-3xl font-semibold">المعاملت الصابقه</p></div>


//         {showTransactionForm && <Card className=" mb-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
//                    <Header className="font-headline-md text-headline-md text-on-background mb-6" title="اضافت معامله جديده"></Header>
//                    <form>
//                        <div>
//                            <label>قيمت المعامله</label>
//                            <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Name"
//                  placeholder="Amount" value={transactions.AMOUNT} onChange={handleNewTransactionAmount} type ="text"></input></div>
               
//                    <div>
//                        <label>نوع المعامله</label>
//                        <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="نوع"
//                     type ="text" value={transactions.TYPE} onChange={handleNewTransactionType}></input></div>

//                   <div> <label>رقم شحنت المعامله</label>
//                   <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="رقم الشحنه"
//                  type ="text" value={transactions.SHIPMENT_ID} onChange={handleNewTransactionShippingId}></input></div>

//                   <div> <label>وقت المعامله</label>
//                   <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="الوقت"
//                  type ="text" value={transactions.TIME} onChange={handleNewTransactionTime}></input></div>
              
//                   <div className="flex gap-3 pt-2">
//                     <Button
//                    type="submit"
//                    variant="primary"
//                    size="md"
//                    onClick={addNewTransaction}>اضافت المعامله</Button>
//                    <Button
//                    type="button"
//                   variant="outline"
//                    size="md"
//                    onClick={hideTransactionFormFun}>الغا</Button>
//                   </div>
//                   </form>
//                   </Card>}



//     <div className="flex ">
//        <Card className="mt-6 mr-6 w-full">
//       <table className=" mr-6 ml-6 w-full">
//           <thead>
//             <tr className="border-b border-outline-variant">                       
//                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                       القيمه
//                 </th>
//                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   النوع
//                 </th>
//                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   رقم الشحنه
//                 </th>
//                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   الوقت
//                 </th>

//                 <Button onClick={showTransactionFormFun}>اضافت معامله</Button>
            

                
//             </tr>
//           </thead>
//           <tbody>
//             {transactions.map((data)=>(

//                <tr 
                            
//                 key={data.id}
//                 className="border-b border-surface-variant hover:bg-surface-container-low transition">
                  

//                   <td className="p-4"><p className={ ` ${data.TYPE === "Debit" ? "text-green-400" : "text-red-500"}`} >
//                                        {data.AMOUNT}
//                                      </p></td>
//                   <td className="p-4"> <span
//                         className={`px-4 py-1 rounded-full
//                            ${data.TYPE ==="Debit" ? "bg-green-600 bg-error text-on-error" : "bg-error text-on-error"}`}
//                       >
//                                        {data.TYPE}
//                                      </span></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.SHIPMENT_ID}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.TIME}
//                                      </p></td>
//                </tr>
              
//             ))}
            
//           </tbody>
//       </table>
//       </Card>
//     </div>
    
//   </main> 
//    )}
*/}


// shipment page
{/*
//   return(

//     <main>

//        <Card className=" h-30 mt-9 w-full ml-6 mr-6 "><span>
//        <div className="flex">
//         <Button onClick={showMerchantFormFun} className="bg-primary-container p-10 mt-3 mr-4 mb-9">العوده الي صفحت التاجر</Button>
//          <div><p className="mr-100 text-2xl">اسم التاجر</p></div>
//          <div> <p className="mr-30 mt-10">رقم التاجر;123465 </p></div>
//          <div><img src={pfp} alt="profile pic" className="h-20 mr-7" ></img></div>
//        </div>
        
//       </span></Card>


//       <div className="flex mr-160 font-semibold text-3xl mb-3 mt-3">الشحنات</div>



//       {showShipmentForm && <Card className=" mb-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
//                  <Header className="font-headline-md text-headline-md text-on-background mb-6" title="اضافت شحنات"></Header>
//                  <form>
//                      <div>
//                          <label>رقم الشحنه</label>
//                          <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="رقم الشحنه"
//                placeholder="example name" value={shipments.SHIPMENT_ID} onChange={handleNewShipmentId} type ="text"></input></div>
                
//                  <div>
//                      <label>الملاحضات</label>
//                      <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="الملاحضات"
//                   type ="text" value={shipments.NOTES} onChange={handleNewShipmentNotes}></input></div>

//                 <div> <label>اخر تحديث</label>
//                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="اخر تحديث"
//                type ="text" value={shipments.LAST_UPDATE} onChange={handleNewShipmentLastUpdated}></input></div>

//                 <div> <label>الحاله</label>
//                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="الحاله"
//                type ="text" value={shipments.STATUS} onChange={handleNewShipmentStatus}></input></div>

//                <div> <label>السعر الكلي</label>
//                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="السعر الكلي"
//                type ="text" value={shipments.TOTAL_PRICE} onChange={handleNewShipmentTotalPrice}></input></div>

//                <div> <label>رسوم الشحن</label>
//                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="رسوم الشحن"
//                type ="text" value={shipments.SHIPPING_FEE} onChange={handleNewShipmentShippingFee}></input></div>

//                <div> <label>تاريخ الوصول</label>
//                 <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="تاريخ الوصول"
//                type ="text" value={shipments.DATE_DELIVERED} onChange={handleNewShipmentDateDelivered}></input></div>
              
//                 <div className="flex gap-3 pt-2">
//                   <Button
//                  type="submit"
//                  variant="primary"
//                  size="md"
//                  onClick={handleNewShippment}>اضافت شحنه</Button>
//                  <Button
//                  type="button"
//                  variant="outline"
//                  size="md"
//                  onClick={hideShipmentFormFun}>الغا</Button>
//                 </div>
//                 </form>
//                 </Card>}


//       <Card className="mr-6">
//       <table className="w-full mr-6 ml-6">
//           <thead>
//             <tr className="border-b border-outline-variant">                       
//                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                       رقم الشحنه
//                 </th>
                 
//                   <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   ملحضات
//                 </th>

//                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   اخر تحديث
//                 </th> 

//                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   الحاله
//                 </th>
//                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   السعر الكلي
//                 </th>
                
//                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   رسوم الشحن
//                 </th> 
//                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
//                   تاريخ الوصول
//                 </th> 
                

//                 <Button onClick={showShipmentFormFun} className="">اضافت شحنه</Button>
//             </tr>
//           </thead>
//           <tbody>
//             {shipments.map((data)=>(

//                <tr 
                            
//                 key={data.id}
//                 className={`border-b border-surface-variant hover:bg-surface-container-low transition  ${data.STATUS ==="Delivered"? "bg-green-200! hover:bg-green-300!" :"" }`}>
                  

//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.SHIPMENT_ID}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.NOTES}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.LAST_UPDATE}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.STATUS}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.TOTAL_PRICE}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.SHIPPING_FEE}
//                                      </p></td>
//                   <td className="p-4"><p className="text-body-md text-on-surface">
//                                        {data.DATE_DELIVERED}
//                                      </p></td>
//                </tr>
              
//             ))}
            
//           </tbody>
//       </table>
//       </Card> 


//     </main>

//   )
// }

// export const BranchesPage = () =>{

// Inventory page


  //  return(
  //  <main>
  //    <Card className=" h-30 mt-9 w-full ml-6 mr-6 "><span>
  //      <div className="flex">
  //        <Button className="bg-primary-container p-10 mt-3 mr-4 mb-9">العوده الي صفحت التاجر</Button>
  //        <div><p className="mr-100 text-2xl">اسم التاجر</p></div>
  //        <div> <p className="mr-30 mt-10">رقم التاجر:123465 </p></div>
  //        <div><img src={pfp} alt="profile pic" className="h-20 mr-7" ></img></div>
  //      </div>
        
  //      </span></Card>
  //      <div className="flex mr-160 text-3xl font-semibold">
  //        <h1>المخزون</h1>
  //      </div>


  //      {showInventoryForm && <Card className=" mb-6 mr-6 newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
  //                 <Header className="font-headline-md text-headline-md text-on-background mb-6" title="اضافت منتاج جديد"></Header>
  //                 <form>
  //                     <div>
  //                         <label>اسم المنتاج</label>
  //                         <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Name"
  //               placeholder="الاسم" value={testInventory.NAME} onChange={handleNewInventoryName} type ="text"></input></div>
              
  //                 <div>
  //                     <label>نوع المنتج</label>
  //                     <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="النوع"
  //                  type ="text" value={testInventory.TYPE} onChange={handleNewInventoryType}></input></div>

  //                <div> <label>مخزون المنتج</label>
  //                <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="المخزون"
  //               type ="text" value={testInventory.STOCK} onChange={handleNewInventoryStock}></input></div>

  //                <div> <label>متوافر عند</label>
  //                <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="الفرع"
  //               type ="text" value={testInventory.BRANCH} onChange={handleNewInventoryAvailbleAt}></input></div>
              
  //                <div className="flex gap-3 pt-2">
  //                  <Button
  //                 type="submit"
  //                 variant="primary"
  //                 size="md"
  //                 onClick={handleAddInventory}>اضافت منتاج</Button>
  //                 <Button
  //                 type="button"
  //                 variant="outline"
  //                 size="md"
  //                 onClick={hideInventoryFormFun}>الغا</Button>
  //                </div>
  //                </form>
  //                </Card>}

  //    <Card className="mr-6">
  //     <table className="w-full mr-6 ml-6">
  //         <thead>
  //           <tr className="border-b border-outline-variant">                       
  //                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
  //                     الاسم
  //               </th>
  //                 <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
  //                 النوع
  //               </th>
  //               <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
  //                 المخزون
  //               </th>
  //               <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
  //                 متوافر عند
  //               </th>
  //                {/* <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
  //                 Add product
  //               </th>  

  //               <Button onClick={showInventoryFormFun}>اضافت منتاج</Button>
  //           </tr>
  //         </thead>
  //         <tbody>
  //           {testInventory.map((data)=>(

  //              <tr 
                            
  //               key={data.id}
  //               className="border-b border-surface-variant hover:bg-surface-container-low transition">
                  

  //                 <td className="p-4"><p className="text-body-md text-on-surface">
  //                                      {data.NAME}
  //                                    </p></td>
  //                 <td className="p-4"><p className="text-body-md text-on-surface">
  //                                      {data.TYPE}
  //                                    </p></td>
  //                 <td className="p-4"><p className="text-body-md text-on-surface">
  //                                      {data.STOCK}
  //                                    </p></td>
  //                 <td className="p-4"><p className="text-body-md text-on-surface">
  //                                      {data.BRANCH}
  //                                    </p></td>
  //              </tr>
              
  //           ))}
            
  //         </tbody>
  //     </table>
  //     </Card> 
  //    </main>
  //  )}
  */}