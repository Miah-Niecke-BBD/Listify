

 export async function GetJwtToken<LoginResponse>( code:string): Promise<LoginResponse | null> {
  try {
    const response = await fetch("http://localhost:8080/authentication?code="+code);
    
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    const data: LoginResponse = await response.json();
    
    return data;
  } catch(error:any){
    console.log((`Failed to get Jwt Token (status ${error.status}): ${error.message}`))
    throw error;
  }
  
}

export async function GetAuthCode<AuthCodeResponse> ():Promise<AuthCodeResponse | null> {
  try{
    const response = await fetch("http://localhost:8080/login");

    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    const data: AuthCodeResponse = await response.json();
    return data;
  }catch(error:any){
    console.log((`Failed to get auth code (status ${error.status}): ${error.message}`))
    throw error;
  }
}

