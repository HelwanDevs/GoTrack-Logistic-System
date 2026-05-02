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
    //  {const headers =[
    //                     {
    //                         id: 1,
    //                         KEY: "NAME",
    //                         Label :"Name"
    //                     },
    //                     {
    //                         id: 2,
    //                         KEY: "LOCATION",
    //                         Label:"Location"
    //                     },
    //                     {
    //                         id:3,
    //                         KEY: "PHONE",
    //                         Label: "Phone"
    //                     },
    //                     {
    //                         id: 4,
    //                         KEY: "DEL",
    //                         Label: "Del"
    //                     },
    //                     {
    //                         id:5,
    //                         KEY:"EDIT",
    //                         Label:"Edit"
    //                     }
    //                 ]}

    // {const [testData,setTestData] =useState([
    // {
    // id:1 ,
    // NAME: "branch1",
    // LOCATION: "Helwan" ,
    // PHONE:"123" ,
    //     DEL: "no" ,
    // },
    // {
    //     id:2 ,
    //     NAME:"branche2" ,
    //     LOCATION:"makram" ,
    //     PHONE:"456" ,
    //     DEL:"yes" ,
    // },
    // {
    //     id: 3,
    //     NAME:"Branch3" ,
    //     LOCATION:"shobra" ,
    //     PHONE: "789" ,
    //     DEL: "no" ,
    // }
    // ])}
    // {const data = testData}
    return(
        <main className="flex-1 p-6 lg:p-10 flex flex-col gap-8">
            <Header
             title="Branch management"
             subtitle="add and update branch info"
             >

             </Header>
             {/* new branch form */}
             <Card className="bg-surface-container-low border-2 border-secondary-container/20"> 
                <Header className="font-headline-md text-headline-md text-on-background mb-6" title="Add new branch"></Header>
                <form>
                    <div>
                        <label>Branch Name</label>
                        <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="Branch Name"
              placeholder="example name" type ="text"></input></div>
                
                <div>
                    <label>Branch Location</label>
                    <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" label="location" placeholder="example Place"
                 type ="text"></input></div>

               <div> <label>Phone number</label>
               <input className="m-1 p-2 w-full border bg-surface-container-low rounded-lg" placeholder="PhoneNum"
              type ="text"></input></div>
              
               <div className="flex gap-3 pt-2">
                 <Button
                type="submit"
                variant="primary"
                size="md">Add Branch</Button>
                <Button
                type="button"
                variant="outline"
                size="md">Cancle</Button>
               </div>
              
              </form>
             </Card>
             {/* add the filter here */}
             <Card>
                <div className = "mb-6">
                    <h3 className="font-headline-md text-headline-md text-on-background mb-1">
                        Filter Branches
                    </h3>
                </div>
                <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
                    <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
                         <div className="col-span-1 md:col-span-4"></div>
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
                     {/* <tbody>
                        {testData.map((row,index) => (
                       <tr key={index}>
                            {headers.map((header,index)=>{
                                return(
                                    <td key ={index}>
                                        {row[header.KEY]}
                                    </td>
                                )
                            })}
                       </tr> 
                        ))}
                     </tbody> */}
                     <tbody>
                        <tr className="border-b border-surface-variant hover:bg-surface-container-low transition">
                            <td className="p-4">test name</td>
                            <td className="p-4">Helwan</td>
                            <td className="p-4 ">
                                <span className= " p-2 rounded-full bg-secondary-container text-on-primary">   123456789   
                                    </span>
                                </td>
                            <td className={`p-6 text-body-sm`}>
                                <span className="px-4 py-1 rounded-full bg-surface-variant text-on-surface-variant"
>                                   no
                                  
                                </span>
                            </td>
                            <td>
                               <div className="flex gap-2"> <Button variant="secondary"
                              size="sm"> edit</Button>
                                <Button variant="outline"
                              size="sm"> delete</Button> </div>
                            </td>
                        </tr>
                     </tbody>
                 </table>
             </Card>
        </main>
    )
}