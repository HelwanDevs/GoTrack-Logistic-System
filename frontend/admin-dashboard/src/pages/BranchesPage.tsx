import { useEffect, useState } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
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

export const BranchesPage = () =>{
     const headers =[
                        {
                            id: 1,
                            KEY: "NAME",
                            Label :"Name"
                        },
                        {
                            id: 2,
                            KEY: "LOCATION",
                            Label:"Location"
                        },
                        {
                            id:3,
                            KEY: "PHONE",
                            Label: "Phone"
                        },
                        {
                            id: 4,
                            KEY: "DEL",
                            Label: "Del"
                        },
                        {
                            id:5,
                            KEY:"EDIT",
                            Label:"Edit"
                        }
                    ]




                 


    const [testData,setTestData] =useState([
    {
    id:"1" ,
    NAME: "branch1",
    LOCATION: "Helwan" ,
    PHONE:"123123123" ,
        DEL: "no" ,
    },
    {
        id:"2" ,
        NAME:"branche2" ,
        LOCATION:"makram" ,
        PHONE:"456456456" ,
        DEL:"yes" ,
    },
    {
        id: "3",
        NAME:"Branch3" ,
        LOCATION:"shobra" ,
        PHONE: "789789798" ,
        DEL: "no" ,
    }
    ])
    const data = testData
    const [branchLocation, setBranchLocation] = useState("");
    const [branchName, setBranchName] = useState("");
    const [branchPone, setBranchPhone] = useState("");
    
    function handleAddBranch(){

        const newBranch ={
            id: String(testData.length + 1),
            NAME: branchName,
            LOCATION: branchLocation,
            PHONE: branchPone,
            DEL: "no"
        }
        setTestData(testData => [...testData, newBranch])
        

    }
    // new branch functions

    let deleteOptions =["yes","no"];
    function handleNameChange (event){
        setBranchName (event.target.value);
    }

    function handleLocationChange (event){
        setBranchLocation (event.target.value);
    }

    function handlePhoneChange(event){
        setBranchPhone (event.target.value);
    }

    const[showForm,setShowForm] = useState(false);
    const showFormFun = (e) => {
        setShowForm(true)
    }
    const hideFormFun = (e) =>{
        setShowForm(false)
    }

    // filter functions
    const handleHideDel= (e) =>{
        if(deletedFilter){
            setDeletedFilter(false)
            setHideDel(["yes"])
        }else{
            setHideDel(["no"])
            setDeletedFilter(true)
        }


    }

   const handleRemoveItem= (index) =>{
    setListBrances(b=>b.filter((element,i)=> i !==index));
    console.log("this should work")

   };


   interface Account extends BranchResponse {}

interface BranchResponse {
  id: string;
  NAME: string;
  LOCATION: string;
  PHONE: string;
  DEL: string;}

    interface EditingBranch {
      id: string;
      NAME: string;
      LOCATION: string;
      PHONE: string;
      DEL: string;
    }



    
      const [editForm, setEditForm] = useState<EditingBranch>({
        id: "" ,
        NAME: "",
        LOCATION: "",
        PHONE: "",
        DEL: "",
        });



const [editingId,setEditingId] = useState ()




    const handleEditClick = (account: Account) => {
    setEditingId(account.id);
    setEditForm({
      id: account.id,
      NAME: account.NAME,
      LOCATION: "",
      PHONE: account.PHONE,
      DEL: account.DEL ?? "no",
    });
    // setFormErrors({});
  };


    const locations = [{value:"helwan" ,label:"helwan"} ,{
         value: "makram" ,label:"makram"},
         {value: "shobra", label:"shobra"},
         {value: "rehab", label: "rehab"}];
    const [deletedFilter, setDeletedFilter] = useState(false);
    const [branchSearch,setBranchSearch] = useState ("");
    const [listBranches,setListBrances] = useState(testData);
    const [hideDel,setHideDel] = useState(["no"])
    const [branchLocationFilter,setBranchLocationFilter] = useState([])
    const [filteredLocation,setFilteredLocation] = useState(testData)


     const handleLocationFilter =(e) =>{
        setBranchLocationFilter(e.target.value)
    }

    useEffect(()=>{
 filterLocation();
 console.log("it's still broken but new now")
    },[setBranchLocationFilter])

    const filterLocation = ()=>{
        if(branchLocationFilter.length >0 ){
            let tempLocation = branchLocationFilter.map((locations)=>{
             let tempData = filteredLocation.filter((data)=> data.LOCATION ===locations)
            setFilteredLocation(tempData)
             return tempData}
            )
            
        }
    }

useEffect(()=>{
    hideDeleted();
},hideDel)
    const hideDeleted = () => {
        if(!deletedFilter){
            
            let tempDel = "yes" 
            let tempData = filteredLocation.filter((data) =>data.DEL !== tempDel);
            setListBrances(tempData)
        }
        else{
            setListBrances(filteredLocation)
        };
      
    }

    return(
        
        <main className="flex-1 p-6 lg:p-10 flex flex-col gap-8">
            <Header
             title="Branch management"
             subtitle="add and update branch info"
             actions ={
                <Button
                variant="primary"
                size="sm"
                onClick={showFormFun}
                >Add New Branch+</Button>
             }
             >

             </Header>
             {/* new branch form */}
             {showForm && <Card className="newBranchForm bg-surface-container-low border-2 border-secondary-container/20"> 
                <Header className="font-headline-md text-headline-md text-on-background mb-6" title="Add new branch"></Header>
                <form>
                    <div>
                        <label>Branch Name</label>
                        <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Branch Name"
              placeholder="example name" value={testData.NAME} onChange={handleNameChange} type ="text"></input></div>
                
                <div>
                    <label>Branch Location</label>
                    <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="example Place"
                 type ="text" value={testData.LOCATION} onChange={handleLocationChange}></input></div>

               <div> <label>Phone number</label>
               <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="PhoneNum"
              type ="text" value={testData.PHONE} onChange={handlePhoneChange}></input></div>
              
               <div className="flex gap-3 pt-2">
                 <Button
                type="submit"
                variant="primary"
                size="md"
                onClick={handleAddBranch}>Add Branch</Button>
                <Button
                type="button"
                variant="outline"
                size="md"
                onClick={hideFormFun}>Cancle</Button>
               </div>
              
              </form>
             </Card>}
             {/* filter branches */}
             <Card>
                <div className = "mb-6">
                    <h3 className="font-headline-md text-headline-md text-on-background mb-1">
                        Filter Branches
                    </h3>
                     <p className="text-body-sm text-on-surface-variant">
                      Number of branches : {listBranches.length}
                    </p>
                </div>
                <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
                    <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
                         <div className="col-span-1 md:col-span-4">
                            <label>search branch name</label>
                            <Input type="text" value={branchSearch} placeholder="branch name" onChange={(e) =>setBranchSearch(e.target.value)}></Input>
                         </div>

                         <div className="col-span-1 md:col-span-4">
                            <label>Search branch location</label>
                            <Select
                            onChange={(e)=>{handleLocationFilter}}
                            options={locations}
                            ></Select>
                         </div>
                         <div className= "col-span-1 md:col-span-2 flex items-center">
                             <Checkbox
                                label="تضمين المحذوف"
                                checked={deletedFilter}
                                size="3xl"
                                onChange={(e) => {
                                  handleHideDel(e);
                                }}
                              />
                         </div>

                          <div className="col-span-1 md:col-span-2 flex items-end">
                            <Button
                              variant="outline"
                              size="md"
                              fullWidth
                              onClick={() => {
                                setBranchSearch("");
                              //   setRoleFilter("");
                              //   setPage(0);
                              }}
                            >
                              إعادة تعيين الفلاتر
                            </Button>
                        </div>
                    </div>
                </div>
             </Card>

                {/* list branches */}
             <Card>
                
                 <div className = "mb-6">
                    <h3 className="font-headline-md text-headline-md text-on-background mb-1">
                        Branches
                    </h3>
                 </div>
                 <table className="w-full">
                   
                     <thead>
                        <tr className="border-b border-outline-variant">

                                

                            <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                                Name
                            </th>
                            <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                              location
                            </th>
                            <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                              Phone number
                            </th>
                            <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                              محذوف
                            </th>
                            <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                              Edit
                            </th>
                        </tr>
                     </thead>
                     <tbody>

                        {listBranches.filter((data, index) =>{
                            return branchSearch.toLowerCase() === "" ? data : data.NAME.toLowerCase().includes(branchSearch)
                        }).map((data) =>(
                            <tr 
                            
                             key={data.id}
                              className="border-b border-surface-variant hover:bg-surface-container-low transition">

                                    
                                
                                <td className="p-4">
                                   {editingId === data.id ? (
                                    <Input
                                      type="email"
                                      value={editForm.NAME}
                                      onChange={(e) =>
                                        setEditForm({
                                          ...editForm,
                                          NAME: e.target.value,
                                        })
                                      }
                                      className="text-body-sm"
                                    />
                                  ) : (
                                    <p className="text-body-md text-on-surface">
                                      {data.NAME}
                                    </p>
                                  )}
                                </td>

                                <td className="p-4">
                                   {editingId === data.id ? (
                                      <Input
                                        type="text"
                                        value={editForm.LOCATION}
                                        onChange={(e) =>
                                          setEditForm({
                                            ...editForm,
                                            LOCATION: e.target.value,
                                          })
                                        }
                                        className="text-body-sm"
                                      />
                                    ) : (
                                      <p className="text-body-md text-on-surface">
                                        {data.LOCATION}
                                      </p>
                                    )}
                                </td>

                                     <td className="p-4">
                                      {editingId === data.id ? (
                                    <Input
                                      type="text"
                                      value={editForm.PHONE}
                                      onChange={(e) =>
                                        setEditForm({
                                          ...editForm,
                                          PHONE: e.target.value,
                                        })
                                      }
                                      className="text-body-sm"
                                    />
                                  ) : (
                                  <span className= " p-2 rounded-full bg-secondary-container text-on-primary">{data.PHONE} </span>
                                  )}
                                </td>
                                <td
                                 className="p-4">
                                   <span
                        className={`px-4 py-1 rounded-full ${data.DEL =="yes" ? "bg-error text-on-error" : "bg-surface-variant text-on-surface-variant"}`}
                      > {data.DEL =="yes" ? "نعم" : "لا"}</span>
                                </td>


                                    
<td className="p-4">
                      <div className="flex gap-2">
                        {editingId === data.id ? (
                          <>
                            <Button
                              variant="primary"
                              size="sm"
                            //   onClick={handleEditAccount}
                            //   disabled={updateMutation.isPending}
                            //   isLoading={updateMutation.isPending}
                            >
                              حفظ
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                            //   onClick={() => {
                            //     setEditingId(null);
                            //     setFormErrors({});
                            //   }}
                            >
                              إلغاء
                            </Button>
                          </>
                        ) : (
                          <>
                           <Button
                                    variant="secondary"
                                    size="sm"
                                     onClick={() => handleEditClick(data)}>Edit</Button>
                             <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={()=> handleRemoveItem(data.id)}>delete</Button>
                          </>
                        )}
                      </div>
                    </td>
                              </tr>
                        ))}


                     </tbody>
                   
                 </table>
             </Card>
        </main>
    )
}